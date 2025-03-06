package io.fromto.domain.usecase

import io.fromto.domain.model.HistoryItem
import io.fromto.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class GetHistoryUseCase(
    private val repository: HistoryRepository
) {
    operator fun invoke(coroutineContext: CoroutineContext): Flow<List<HistoryItem>> = repository.historyStream(context = coroutineContext)
}
