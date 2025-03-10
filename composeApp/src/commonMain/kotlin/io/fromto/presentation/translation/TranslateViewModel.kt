package io.fromto.presentation.translation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.fromto.domain.model.AppLocale
import io.fromto.domain.model.HistoryItem
import io.fromto.domain.model.Language
import io.fromto.domain.model.TranslationParams
import io.fromto.domain.usecase.ChangeLocaleUseCase
import io.fromto.domain.usecase.GetCurrentLocaleUseCase
import io.fromto.domain.usecase.GetSelectedLanguagesUseCase
import io.fromto.domain.usecase.SaveHistoryUseCase
import io.fromto.domain.usecase.TranslateUseCase
import io.fromto.domain.usecase.UpdateSelectedLanguagesUseCase
import io.fromto.domain.util.onError
import io.fromto.domain.util.onSuccess
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(FlowPreview::class)
class TranslateViewModel(
    private val translateUseCase: TranslateUseCase,
    private val changeLocaleUseCase: ChangeLocaleUseCase,
    private val saveHistoryUseCase: SaveHistoryUseCase,
    getSelectedLanguagesUseCase: GetSelectedLanguagesUseCase,
    private val updateSelectedLanguagesUseCase: UpdateSelectedLanguagesUseCase,
    getCurrentLocaleUseCase: GetCurrentLocaleUseCase
) : ViewModel() {

    private var translationJob: Job? = null
    private var saveJob: Job? = null
    private var lastSavedText = ""

    private val _state =
        MutableStateFlow(
            TranslateState(
                locale = AppLocale.fromCode(getCurrentLocaleUseCase()),
                fromLanguage = getSelectedLanguagesUseCase().from,
                toLanguage = getSelectedLanguagesUseCase().to
            )
        )
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TranslateState()
    )

    private val textChangeChannel = Channel<String>(capacity = Channel.CONFLATED)

    init {
        // Start listening to debounced text changes
        viewModelScope.launch {
            textChangeChannel.receiveAsFlow()
                .debounce(TRANSLATE_DEBOUNCE_TIME)
                .distinctUntilChanged()
                .collectLatest { text ->
                    translate(text)
                }
        }
    }

    fun onEvent(event: TranslateEvent) {
        when (event) {
            is TranslateEvent.EnterText -> enterText(event.text)
            is TranslateEvent.SelectFromLanguage -> selectFromLanguage(event.language)
            is TranslateEvent.SelectToLanguage -> selectToLanguage(event.language)
            is TranslateEvent.SelectLocale -> selectLocale(event.appLocale)
            is TranslateEvent.SaveTranslation -> saveTranslation(event.isFocused)
            is TranslateEvent.SelectHistoryItem -> selectHistoryItem(event)
            TranslateEvent.SwapLanguages -> swapLanguages()
            TranslateEvent.ClearText -> clearText()
            TranslateEvent.ClearError -> clearError()
        }
    }

    private fun selectHistoryItem(event: TranslateEvent.SelectHistoryItem) {
        translationJob?.cancel()
        _state.update {
            it.copy(
                fromText = event.sourceText,
                toText = event.translatedText,
                fromLanguage = Language.fromCode(event.sourceLang),
                toLanguage = Language.fromCode(event.targetLang)
            )
        }
    }

    private fun selectLocale(appLocale: AppLocale) {
        _state.update {
            it.copy(locale = appLocale)
        }
        changeLocaleUseCase(appLocale.languageCode)
    }

    private fun enterText(text: String) {
        _state.update { it.copy(fromText = text) }
        viewModelScope.launch {
            textChangeChannel.send(text)
        }
    }

    private fun selectFromLanguage(language: Language) {
        _state.update {
            val shouldSwap = language.code == it.toLanguage.code
            it.copy(
                fromLanguage = language,
                toLanguage = if (shouldSwap) it.fromLanguage else it.toLanguage,
                toText = if (shouldSwap) "" else it.toText
            )
        }
        viewModelScope.launch {
            updateSelectedLanguagesUseCase(language, _state.value.toLanguage)
        }
        translate(_state.value.fromText)
    }

    private fun selectToLanguage(language: Language) {
        _state.update {
            val shouldSwap = language.code == it.fromLanguage.code
            it.copy(
                toLanguage = language,
                fromLanguage = if (shouldSwap) it.toLanguage else it.fromLanguage,
                toText = if (shouldSwap) "" else it.toText
            )
        }
        viewModelScope.launch {
            updateSelectedLanguagesUseCase(_state.value.fromLanguage, language)
        }
        translate(_state.value.fromText)
    }

    private fun swapLanguages() {
        _state.update {
            it.copy(
                fromLanguage = it.toLanguage,
                toLanguage = it.fromLanguage,
                fromText = it.toText.ifBlank { "" },
                toText = it.fromText
            )
        }
        viewModelScope.launch {
            updateSelectedLanguagesUseCase(_state.value.toLanguage, _state.value.fromLanguage)
        }
    }

    private fun translate(text: String) {
        if (state.value.isTranslating || text.isBlank()) {
            return
        }

        translationJob?.cancel()

        translationJob = viewModelScope.launch {
            _state.update { it.copy(isTranslating = true) }

            val resultCase = when (_state.value.toLanguage) {
                Language.UZBEK_CYRILLIC, Language.KARAKALPAK_CYRILLIC -> "cyrill"
                else -> "latin"
            }

            val result = translateUseCase(
                TranslationParams(
                    sourceLang = _state.value.fromLanguage,
                    targetLang = _state.value.toLanguage,
                    resultCase = resultCase,
                    text = text
                )
            )

            result.onSuccess { toText ->
                _state.update {
                    it.copy(toText = toText)
                }
            }.onError { error ->
                _state.update {
                    it.copy(error = error)
                }
            }

            _state.update { it.copy(isTranslating = false) }
        }
    }

    private fun clearText() {
        translationJob?.cancel()
        _state.update {
            it.copy(
                fromText = "",
                toText = "",
                isTranslating = false,
                error = null,
            )
        }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun saveTranslation(isFocused: Boolean) {
        if (isFocused) {
            saveJob?.cancel()
        } else {
            saveJob = viewModelScope.launch {
                // Small delay to catch rapid focus changes
                delay(SAVE_DEBOUNCE_TIME)
                if (shouldSaveToHistory(_state.value.fromText, _state.value.toText)) {
                    saveHistoryUseCase(
                        HistoryItem(
                            sourceText = _state.value.fromText,
                            translatedText = _state.value.toText,
                            sourceLang = _state.value.fromLanguage.code,
                            targetLang = _state.value.toLanguage.code,
                            id = Uuid.random().toString(),
                            timestamp = Clock.System.now().toEpochMilliseconds()
                        )
                    )
                    lastSavedText = _state.value.fromText
                }
            }
        }
    }

    private fun shouldSaveToHistory(original: String, translated: String): Boolean {
        return original.isNotBlank() &&
                translated.isNotBlank() &&
                original != translated &&
                original != lastSavedText &&
                original.length >= MIN_SAVE_LENGTH
    }

    override fun onCleared() {
        super.onCleared()
        textChangeChannel.close()
    }

    companion object {
        private const val TRANSLATE_DEBOUNCE_TIME = 300L
        private const val SAVE_DEBOUNCE_TIME = 500L
        private const val MIN_SAVE_LENGTH = 3
    }
}