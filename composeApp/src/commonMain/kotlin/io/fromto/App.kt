package io.fromto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.fromto.analytics.FirebaseAnalyticsHelper
import io.fromto.analytics.LocalAnalytics
import io.fromto.presentation.main.MainScreen
import io.fromto.presentation.theme.TranslatorTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val analytics = FirebaseAnalyticsHelper()
    CompositionLocalProvider(
        LocalAnalytics provides analytics
    ) {
        TranslatorTheme {
            MainScreen()
        }
    }
}
