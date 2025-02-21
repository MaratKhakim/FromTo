package io.fromto

import androidx.compose.runtime.Composable
import io.fromto.presentation.theme.TranslatorTheme
import io.fromto.presentation.translation.TranslationScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    TranslatorTheme {
        TranslationScreen()
    }
}
