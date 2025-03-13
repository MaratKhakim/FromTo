package io.fromto.data.datasource.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.fromto.data.mapper.toHistoryItem
import io.fromto.database.TranslateDatabase
import io.fromto.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext
import kotlinx.datetime.Clock

class SqlHistoryDataSource(
    db: TranslateDatabase
): HistoryLocalDataSource {

    private val queries = db.translateQueries

    override suspend fun insertTranslation(item: HistoryItem) {
        queries.insert(
            id = item.id,
            sourceText = item.sourceText,
            translatedText = item.translatedText,
            sourceLang = item.sourceLang,
            targetLang = item.targetLang,
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
    }

    override fun historyStream(context: CoroutineContext): Flow<List<HistoryItem>> {
        return queries.getHistory()
            .asFlow()
            .mapToList(context)
            .map { history -> history.map { it.toHistoryItem() } }
    }

    override suspend fun deleteTranslation(id: String) {
        queries.delete(id)
    }

    override suspend fun deleteAllTranslations() {
        queries.deleteAll()
    }

    override suspend fun deleteOldHistory(limit: Long) {
        queries.deleteOldHistory(limit)
    }
}