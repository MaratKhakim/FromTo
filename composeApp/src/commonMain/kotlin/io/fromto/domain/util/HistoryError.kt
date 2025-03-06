package io.fromto.domain.util

sealed class HistoryError: Error {
    data object DeleteError : HistoryError()
}