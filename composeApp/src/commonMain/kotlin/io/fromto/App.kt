package io.fromto

import androidx.compose.runtime.Composable
import io.fromto.di.platformModule
import io.fromto.di.sharedModule
import io.fromto.presentation.main.MainScreen
import io.fromto.presentation.theme.TranslatorTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    TranslatorTheme {
        KoinApplication(
            application = {
                modules(sharedModule, platformModule)
            }
        ) {
            MainScreen()
        }
    }
}
