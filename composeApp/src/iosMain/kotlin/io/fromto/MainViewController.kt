package io.fromto

import androidx.compose.ui.window.ComposeUIViewController
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import dev.gitlive.firebase.initialize
import io.fromto.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initializeKoin()
        Firebase.initialize()
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)
    }
) { App() }