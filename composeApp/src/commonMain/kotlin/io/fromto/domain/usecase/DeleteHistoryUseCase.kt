package io.fromto.domain.usecase

import io.fromto.domain.repository.HistoryRepository

class DeleteHistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(id: String) = repository.deleteTranslation(id)
}