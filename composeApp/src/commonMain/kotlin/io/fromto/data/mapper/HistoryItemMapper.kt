package io.fromto.data.mapper

import io.fromto.database.HistoryEntity
import io.fromto.domain.model.HistoryItem

fun HistoryEntity.toHistoryItem(): HistoryItem {
    return HistoryItem(
        id = id,
        sourceLang = sourceLang,
        sourceText = sourceText,
        targetLang = targetLang,
        timestamp = timestamp,
        translatedText = translatedText
    )
}