package io.fromto.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TranslateResponseDto(
    val result: String,
)