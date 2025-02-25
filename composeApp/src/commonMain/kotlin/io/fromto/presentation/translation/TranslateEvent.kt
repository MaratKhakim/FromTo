package io.fromto.presentation.translation

import io.fromto.domain.model.Language

sealed class TranslateEvent {
    data class EnterText(val text: String) : TranslateEvent()
    data class SelectFromLanguage(val language: Language) : TranslateEvent()
    data class SelectToLanguage(val language: Language) : TranslateEvent()
    data object SwapLanguages : TranslateEvent()
    data object ClearText : TranslateEvent()
    data object ClearError : TranslateEvent()
}