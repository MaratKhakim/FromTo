package io.fromto.domain.usecase

import io.fromto.domain.model.TranslationParams
import io.fromto.domain.repository.TranslateRepository
import io.fromto.domain.util.Result
import io.fromto.domain.util.TranslateError

class TranslateUseCase(
    private val repository: TranslateRepository,
) {
    suspend operator fun invoke(translationParams: TranslationParams): Result<String, TranslateError> =
        repository.translate(translationParams)
}