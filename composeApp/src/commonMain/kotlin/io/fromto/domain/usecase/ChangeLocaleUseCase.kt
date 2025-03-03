package io.fromto.domain.usecase

import io.fromto.domain.repository.LocalizationRepository

class ChangeLocaleUseCase(private val repository: LocalizationRepository) {
    operator fun invoke(languageCode: String) {
        repository.changeLocale(languageCode)
    }
}
