package io.fromto.di

import app.cash.sqldelight.db.SqlDriver
import io.fromto.data.datasource.local.DatabaseDriverFactory
import io.fromto.domain.util.LocaleManager
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single<LocaleManager> { LocaleManager(context = androidContext()) }
        single<SqlDriver> { DatabaseDriverFactory(context = androidContext()).create() }
    }