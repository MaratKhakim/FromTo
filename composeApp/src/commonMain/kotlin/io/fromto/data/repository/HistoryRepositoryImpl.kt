package io.fromto.data.repository

import io.fromto.data.datasource.local.HistoryLocalDataSource
import io.fromto.domain.model.HistoryItem
import io.fromto.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class HistoryRepositoryImpl(
    private val dataSource: HistoryLocalDataSource
) : HistoryRepository {
    override suspend fun saveTranslation(item: HistoryItem) {
        dataSource.insertTranslation(item)
    }

    override suspend fun deleteTranslation(id: String) {
        dataSource.deleteTranslation(id)
    }
    override suspend fun deleteTranslations() {
        dataSource.deleteAllTranslations()
    }

    override suspend fun deleteOldTranslations(limit: Long) {
        dataSource.deleteOldHistory(limit)
    }

    override fun historyStream(context: CoroutineContext): Flow<List<HistoryItem>> {
        return dataSource.historyStream(context)
    }
}