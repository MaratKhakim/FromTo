package io.fromto.presentation.translation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.fromto.domain.model.AppLocale
import io.fromto.domain.model.Language
import io.fromto.domain.model.TranslationParams
import io.fromto.domain.usecase.ChangeLocaleUseCase
import io.fromto.domain.usecase.GetCurrentLocaleUseCase
import io.fromto.domain.usecase.TranslateUseCase
import io.fromto.domain.util.onError
import io.fromto.domain.util.onSuccess
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class TranslateViewModel(
    private val translateUseCase: TranslateUseCase,
    private val changeLocaleUseCase: ChangeLocaleUseCase,
    getCurrentLocaleUseCase: GetCurrentLocaleUseCase
) : ViewModel() {

    private var translationJob: Job? = null

    private val _state =
        MutableStateFlow(
            TranslateState(
                locale = AppLocale.fromCode(getCurrentLocaleUseCase())
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
                .debounce(300)
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
            TranslateEvent.SwapLanguages -> swapLanguages()
            TranslateEvent.ClearText -> clearText()
            TranslateEvent.ClearError -> clearError()
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

    override fun onCleared() {
        super.onCleared()
        textChangeChannel.close()
    }
}