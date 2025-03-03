package io.fromto.domain.model

enum class AppLocale(val languageCode: String, val languageLabel: String) {
    Karakalpak(languageCode = "kaa", languageLabel = "Qaraqalpaqsha"),
    English(languageCode = "en", languageLabel = "English"),
    Russian(languageCode = "ru", languageLabel = "Русский"),
    Uzbek(languageCode = "uz", languageLabel = "O’zbekcha"),;

    companion object {
        fun fromCode(code: String): AppLocale =
            entries.find { it.languageCode == code } ?: English
    }
}