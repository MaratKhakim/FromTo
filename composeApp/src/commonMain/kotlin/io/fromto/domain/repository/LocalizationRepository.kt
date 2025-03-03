package io.fromto.domain.repository

interface LocalizationRepository {
    fun changeLocale(languageCode: String)
    fun getCurrentLocale(): String
}
