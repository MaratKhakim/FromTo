package io.fromto

import androidx.compose.ui.window.ComposeUIViewController
import io.fromto.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App() }