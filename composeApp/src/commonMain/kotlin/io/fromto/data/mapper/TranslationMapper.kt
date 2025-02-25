package io.fromto.data.mapper

import io.fromto.data.dto.RequestBody
import io.fromto.data.dto.TranslationRequestDto
import io.fromto.domain.model.TranslationParams

fun TranslationParams.toDto() = TranslationRequestDto(
    RequestBody(
        text = text,
        sourceLangCode = sourceLang.code,
        targetLangCode = targetLang.code,
        resultCase = resultCase
    )
)
