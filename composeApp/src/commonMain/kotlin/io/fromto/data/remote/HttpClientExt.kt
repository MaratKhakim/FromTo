package io.fromto.data.remote

import io.fromto.domain.util.Result
import io.fromto.domain.util.TranslateError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, TranslateError> {
    val response = try {
        execute()
    } catch (e: SocketTimeoutException) {
        return Result.Error(TranslateError.Timeout)
    } catch (e: UnresolvedAddressException) {
        return Result.Error(TranslateError.NetworkUnavailable)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(TranslateError.UnknownError)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, TranslateError> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                Result.Error(TranslateError.SerializationError)
            }
        }

        408 -> Result.Error(TranslateError.Timeout)
        429 -> Result.Error(TranslateError.TooManyRequests)
        in 500..599 -> Result.Error(TranslateError.ServerError)
        else -> Result.Error(TranslateError.UnknownError)
    }
}