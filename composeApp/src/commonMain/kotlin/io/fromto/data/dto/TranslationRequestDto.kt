package io.fromto.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestBody(
    @SerialName("text") val text: String,
    @SerialName("lang_from") val sourceLangCode: String,
    @SerialName("lang_to") val targetLangCode: String,
    @SerialName("resultCase") val resultCase: String
)

@Serializable
data class TranslationRequestDto(
    val body: RequestBody
)