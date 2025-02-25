package io.fromto.data.datasource.remote

import io.fromto.data.dto.TranslateResponseDto
import io.fromto.domain.model.TranslationParams
import io.fromto.domain.util.Result
import io.fromto.domain.util.TranslateError

interface RemoteDataSource {
    suspend fun translate(translationParams: TranslationParams): Result<TranslateResponseDto, TranslateError>
}