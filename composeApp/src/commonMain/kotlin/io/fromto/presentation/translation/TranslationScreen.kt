package io.fromto.presentation.translation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import fromto.composeapp.generated.resources.Res
import fromto.composeapp.generated.resources.text_copied
import io.fromto.presentation.theme.Dimens
import io.fromto.presentation.translation.components.ErrorMessage
import io.fromto.presentation.translation.components.LanguageSelector
import io.fromto.presentation.translation.components.TranslationTextField
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun TranslationScreen(
    state: TranslateState,
    onEvent: (TranslateEvent) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val textCopiedConfirmation = stringResource(Res.string.text_copied)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        BoxWithConstraints {
            val screenHeight = maxHeight

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxSize()
                    .padding(padding)
                    .padding(Dimens.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
            ) {
                TranslationTextField(
                    modifier = Modifier
                        .heightIn(min = screenHeight * 2 / 3),
                    inputText = state.fromText,
                    translatedText = state.toText,
                    isTranslating = state.isTranslating,
                    fromLanguage = state.fromLanguage.name,
                    toLanguage = state.toLanguage.name,
                    onCloseClick = {
                        onEvent(TranslateEvent.ClearText)
                    },
                    onTextChange = {
                        onEvent(TranslateEvent.EnterText(it))
                    },
                    onCopyClick = {
                        clipboardManager.setText(
                            buildAnnotatedString {
                                append(it)
                            }
                        )
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(textCopiedConfirmation)
                        }
                    },
                )

                LanguageSelector(
                    sourceLanguage = state.fromLanguage,
                    targetLanguage = state.toLanguage,
                    onSourceLanguageSelected = {
                        onEvent(TranslateEvent.SelectFromLanguage(it))
                    },
                    onTargetLanguageSelected = {
                        onEvent(TranslateEvent.SelectToLanguage(it))
                    },
                    onSwapLanguages = {
                        onEvent(TranslateEvent.SwapLanguages)
                    }
                )
            }

            state.error?.let {
                ErrorMessage(
                    error = it,
                    onDismiss = {
                        onEvent(TranslateEvent.ClearError)
                    }
                )
            }
        }
    }
}