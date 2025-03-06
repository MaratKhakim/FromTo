package io.fromto.domain.repository

import io.fromto.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface HistoryRepository {
    suspend fun saveTranslation(item: HistoryItem)
    suspend fun deleteTranslation(id: String)
    suspend fun deleteTranslations()
    fun historyStream(context: CoroutineContext): Flow<List<HistoryItem>>
}
