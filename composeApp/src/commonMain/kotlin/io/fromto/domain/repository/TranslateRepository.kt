package io.fromto.domain.repository

import io.fromto.domain.model.TranslationParams
import io.fromto.domain.util.Result
import io.fromto.domain.util.TranslateError

interface TranslateRepository {
    suspend fun translate(translationParams: TranslationParams): Result<String, TranslateError>
}