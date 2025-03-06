package io.fromto.domain.model

data class HistoryItem(
    val id: String,
    val sourceText: String,
    val translatedText: String,
    val sourceLang: String,
    val targetLang: String,
    val timestamp: Long
)