package io.fromto.domain.util

sealed class TranslateError(
    open val code: Int? = null,
    open val message: String? = null,
    open val throwable: Throwable? = null
) : Error {
    data class NetworkUnavailable(
        override val throwable: Throwable?
    ) : TranslateError(throwable = throwable)

    data class ServerError(
        override val code: Int,
        override val message: String,
        override val throwable: Throwable? = null
    ) : TranslateError(code, message, throwable)

    data class SerializationError(
        override val throwable: Throwable?
    ) : TranslateError(throwable = throwable)

    data class UnknownError(
        override val throwable: Throwable?
    ) : TranslateError(throwable = throwable)
}