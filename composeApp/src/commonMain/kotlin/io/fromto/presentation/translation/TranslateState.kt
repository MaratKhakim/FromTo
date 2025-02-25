package io.fromto.presentation.translation

import io.fromto.domain.model.Language
import io.fromto.domain.util.TranslateError

data class TranslateState(
    val fromText: String = "",
    val toText: String = "",
    val isTranslating: Boolean = false,
    val fromLanguage: Language = Language.ENGLISH,
    val toLanguage: Language = Language.KARAKALPAK,
    val error: TranslateError? = null,
)
