package io.fromto.data.datasource.local

import io.fromto.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

interface HistoryLocalDataSource {
    suspend fun insertTranslation(item: HistoryItem)
    fun historyStream(context: CoroutineContext): Flow<List<HistoryItem>>
    suspend fun deleteTranslation(id: String)
    suspend fun deleteAllTranslations()
    suspend fun deleteOldHistory(limit: Long)
}