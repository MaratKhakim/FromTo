package io.fromto.domain.util

sealed class TranslateError: Error {
    data object NetworkUnavailable : TranslateError()
    data object ServerError : TranslateError()
    data object Timeout : TranslateError()
    data object TooManyRequests : TranslateError()
    data object SerializationError : TranslateError()
    data object UnknownError : TranslateError()
}