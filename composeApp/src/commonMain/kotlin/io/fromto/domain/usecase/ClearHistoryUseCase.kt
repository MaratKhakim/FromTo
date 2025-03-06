package io.fromto.domain.usecase

import io.fromto.domain.repository.HistoryRepository

class ClearHistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke() = repository.deleteTranslations()
}