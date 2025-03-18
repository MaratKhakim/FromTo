package io.fromto.data.repository

import com.russhwolf.settings.Settings
import io.fromto.domain.model.AppLocale
import io.fromto.domain.repository.LocalizationRepository
import io.fromto.domain.util.LocaleManager

class LocalizationRepositoryImpl(
    private val localeManager: LocaleManager,
    private val settings: Settings,
) : LocalizationRepository {
    companion object {
        private const val KEY_LOCALE = "savedLanguageIso"
    }

    override fun getCurrentLocale(): String {
        return settings.getString(KEY_LOCALE, AppLocale.English.languageCode)
    }

    override fun changeLocale(languageCode: String) {
        settings.putString(KEY_LOCALE, languageCode)
        localeManager.updateLocale(languageCode)
    }
}