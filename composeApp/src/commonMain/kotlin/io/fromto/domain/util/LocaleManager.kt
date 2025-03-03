package io.fromto.domain.util

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class LocaleManager {
    fun updateLocale(languageCode: String)
}