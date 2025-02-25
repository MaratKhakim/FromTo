package io.fromto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import io.fromto.di.platformModule
import io.fromto.di.sharedModule
import io.fromto.presentation.theme.TranslatorTheme
import io.fromto.presentation.translation.TranslateViewModel
import io.fromto.presentation.translation.TranslationScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    TranslatorTheme {
        KoinApplication(
            application = {
                modules(sharedModule, platformModule)
            }
        ) {
            val viewModel = koinViewModel<TranslateViewModel>()
            val state by viewModel.state.collectAsState()

            TranslationScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
        }
    }
}
