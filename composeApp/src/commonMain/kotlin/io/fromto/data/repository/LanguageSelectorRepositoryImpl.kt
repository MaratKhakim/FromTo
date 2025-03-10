package io.fromto.data.repository

import com.russhwolf.settings.Settings
import io.fromto.domain.model.Language
import io.fromto.domain.repository.LanguageSelectorRepository

class LanguageSelectorRepositoryImpl : LanguageSelectorRepository {
    companion object {
        private const val KEY_FROM_LANGUAGE = "fromLanguage"
        private const val KEY_TO_LANGUAGE = "toLanguage"
    }

    private val settings: Settings = Settings()

    override fun getFromLanguage(): Language {
        val code = settings.getString(KEY_FROM_LANGUAGE, defaultValue = Language.ENGLISH.code)
        return Language.fromCode(code)
    }

    override fun getToLanguage(): Language {
        val code = settings.getString(KEY_TO_LANGUAGE, defaultValue = Language.KARAKALPAK.code)
        return Language.fromCode(code)
    }

    override fun setFromLanguage(language: Language) {
        settings.putString(KEY_FROM_LANGUAGE, language.code)
    }

    override fun setToLanguage(language: Language) {
        settings.putString(KEY_TO_LANGUAGE, language.code)
    }
}

