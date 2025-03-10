package io.fromto.domain.repository

import io.fromto.domain.model.Language

interface LanguageSelectorRepository {
    fun getFromLanguage(): Language
    fun getToLanguage(): Language
    fun setFromLanguage(language: Language)
    fun setToLanguage(language: Language)
}
