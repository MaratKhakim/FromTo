package io.fromto.domain.util

import platform.Foundation.NSUserDefaults

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class LocaleManager {
    actual fun updateLocale(languageCode: String) {
        val defaults = NSUserDefaults.standardUserDefaults
        defaults.setObject(arrayListOf(languageCode), forKey = "AppleLanguages")
        defaults.synchronize()
    }
}