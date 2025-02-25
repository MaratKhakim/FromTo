package io.fromto.data.datasource.remote

import io.fromto.config.ApiConfig
import io.fromto.data.dto.TranslateResponseDto
import io.fromto.data.mapper.toDto
import io.fromto.data.remote.safeCall
import io.fromto.domain.model.TranslationParams
import io.fromto.domain.util.Result
import io.fromto.domain.util.TranslateError
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class KtorRemoteDataSource(
    private val httpClient: HttpClient
) : RemoteDataSource {
    override suspend fun translate(translationParams: TranslationParams): Result<TranslateResponseDto, TranslateError> {
        return safeCall<TranslateResponseDto> {
            httpClient.post(
                urlString = ApiConfig.TRANSLATE
            ) {
                contentType(ContentType.Application.Json)
                setBody(translationParams.toDto())
            }
        }
    }
}
