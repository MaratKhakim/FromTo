package io.fromto.data.repository

import com.russhwolf.settings.Settings
import io.fromto.domain.model.AppLocale
import io.fromto.domain.repository.LocalizationRepository
import io.fromto.domain.util.LocaleManager

const val KEY = "savedLanguageIso"

class LocalizationRepositoryImpl(
    private val localeManager: LocaleManager
) : LocalizationRepository {
    private val settings = Settings()

    override fun getCurrentLocale(): String {
        return settings.getString(KEY, AppLocale.English.languageCode)
    }

    override fun changeLocale(languageCode: String) {
        settings.putString(KEY, languageCode)
        localeManager.updateLocale(languageCode)
    }
}