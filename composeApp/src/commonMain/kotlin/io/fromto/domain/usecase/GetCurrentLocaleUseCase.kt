package io.fromto.domain.usecase

import io.fromto.domain.repository.LocalizationRepository

class GetCurrentLocaleUseCase(private val repository: LocalizationRepository) {
    operator fun invoke(): String {
        return repository.getCurrentLocale()
    }
}