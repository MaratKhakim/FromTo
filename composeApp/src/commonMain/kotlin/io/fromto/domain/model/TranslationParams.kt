package io.fromto.domain.model

data class TranslationParams(
    val text: String,
    val sourceLang: Language,
    val targetLang: Language,
    val resultCase: String,
)
