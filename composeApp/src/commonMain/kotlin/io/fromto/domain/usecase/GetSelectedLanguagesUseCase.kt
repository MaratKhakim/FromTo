package io.fromto.domain.usecase

import io.fromto.domain.model.SelectedLanguages
import io.fromto.domain.repository.LanguageSelectorRepository

class GetSelectedLanguagesUseCase(private val repository: LanguageSelectorRepository) {
    operator fun invoke(): SelectedLanguages {
        return SelectedLanguages(
            from = repository.getFromLanguage(),
            to = repository.getToLanguage()
        )
    }
}
