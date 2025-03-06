package io.fromto.presentation.translation

import io.fromto.domain.model.AppLocale
import io.fromto.domain.model.Language

sealed class TranslateEvent {
    data class EnterText(val text: String) : TranslateEvent()
    data class SelectFromLanguage(val language: Language) : TranslateEvent()
    data class SelectToLanguage(val language: Language) : TranslateEvent()
    data class SelectLocale(val appLocale: AppLocale) : TranslateEvent()
    data object SwapLanguages : TranslateEvent()
    data object ClearText : TranslateEvent()
    data object ClearError : TranslateEvent()
    data class SaveTranslation(
        val sourceText: String,
        val translatedText: String,
        val sourceLang: String,
        val targetLang: String,
        val timestamp: Long
    ) : TranslateEvent()
}