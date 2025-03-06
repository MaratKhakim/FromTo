package io.fromto.domain.usecase

import io.fromto.domain.model.HistoryItem
import io.fromto.domain.repository.HistoryRepository

class SaveHistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(item: HistoryItem) = repository.saveTranslation(item)
}