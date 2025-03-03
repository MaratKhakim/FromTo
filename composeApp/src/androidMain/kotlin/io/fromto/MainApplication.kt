package io.fromto

import android.app.Application
import io.fromto.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin(
            config = { androidContext(this@MainApplication) }
        )
    }
}