package io.fromto.domain.usecase

import io.fromto.domain.model.HistoryItem
import io.fromto.domain.repository.HistoryRepository

class SaveHistoryUseCase(
    private val repository: HistoryRepository,
    private val maxHistorySize: Long = 150
) {
    suspend operator fun invoke(item: HistoryItem) {
        repository.saveTranslation(item)
        repository.deleteOldTranslations(maxHistorySize)
    }
}