package io.fromto.data.remote

import io.fromto.domain.util.Result
import io.fromto.domain.util.TranslateError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.util.network.UnresolvedAddressException

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, TranslateError> {
    return try {
        val response = execute()
        if (response.status.isSuccess()) {
            Result.Success(response.body<T>())
        } else {
            Result.Error(
                TranslateError.ServerError(
                    response.status.value,
                    response.status.description
                )
            )
        }
    } catch (e: ResponseException) {
        Result.Error(
            TranslateError.ServerError(
                e.response.status.value,
                e.response.status.description,
                e
            )
        )
    } catch (e: UnresolvedAddressException) {
        Result.Error(TranslateError.NetworkUnavailable(e))
    } catch (e: NoTransformationFoundException) {
        Result.Error(TranslateError.SerializationError(e))
    } catch (e: Exception) {
        Result.Error(TranslateError.UnknownError(e))
    }
}