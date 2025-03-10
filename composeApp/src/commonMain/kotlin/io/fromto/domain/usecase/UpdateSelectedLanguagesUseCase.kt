package io.fromto.domain.usecase

import io.fromto.domain.model.Language
import io.fromto.domain.repository.LanguageSelectorRepository

class UpdateSelectedLanguagesUseCase(private val repository: LanguageSelectorRepository) {
    operator fun invoke(from: Language, to: Language) {
        val currentFrom = repository.getFromLanguage()
        val currentTo = repository.getToLanguage()

        if (currentFrom != from) {
            repository.setFromLanguage(from)
        }
        if (currentTo != to) {
            repository.setToLanguage(to)
        }
    }
}