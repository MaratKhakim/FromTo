package io.fromto

import androidx.compose.runtime.Composable
import io.fromto.presentation.main.MainScreen
import io.fromto.presentation.theme.TranslatorTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    TranslatorTheme {
        MainScreen()
    }
}
