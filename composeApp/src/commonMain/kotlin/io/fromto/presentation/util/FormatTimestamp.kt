package io.fromto.presentation.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

import kotlinx.datetime.toLocalDateTime

fun formatDate(timestamp: Long): String {
    val localDateTime =
        Instant.fromEpochMilliseconds(timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault())

    return "${localDateTime.dayOfMonth.toString().padStart(2, '0')}." +
            "${localDateTime.monthNumber.toString().padStart(2, '0')}." +
            localDateTime.year
}