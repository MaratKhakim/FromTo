package io.fromto.presentation.translation

import io.fromto.domain.model.AppLocale
import io.fromto.domain.model.Language
import io.fromto.domain.util.TranslateError

data class TranslateState(
    val fromText: String = "",
    val toText: String = "",
    val isTranslating: Boolean = false,
    val fromLanguage: Language = Language.UZBEK,
    val toLanguage: Language = Language.KARAKALPAK,
    val locale: AppLocale = AppLocale.English,
    val error: TranslateError? = null,
)
