package io.fromto

import androidx.compose.runtime.Composable
import io.fromto.di.sharedModule
import io.fromto.presentation.theme.TranslatorTheme
import io.fromto.presentation.translation.TranslationScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    TranslatorTheme {
        KoinApplication(
            application = {
                modules(sharedModule)
            }
        ) {
            TranslationScreen()
        }
    }
}
