package io.fromto.data.repository

import io.fromto.data.datasource.remote.RemoteDataSource
import io.fromto.domain.model.TranslationParams
import io.fromto.domain.repository.TranslateRepository
import io.fromto.domain.util.Result
import io.fromto.domain.util.TranslateError
import io.fromto.domain.util.map

class TranslateRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : TranslateRepository {
    override suspend fun translate(translationParams: TranslationParams): Result<String, TranslateError> {
        return remoteDataSource.translate(translationParams).map {
            it.result
        }
    }
}