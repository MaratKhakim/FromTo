package io.fromto.presentation.translation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.fromto.presentation.theme.Dimens
import io.fromto.presentation.translation.components.ErrorMessage
import io.fromto.presentation.translation.components.LanguageSelector
import io.fromto.presentation.translation.components.TranslationTextField

@Composable
fun TranslationScreen(
    state: TranslateState,
    onCopyClick: (String) -> Unit,
    onEvent: (TranslateEvent) -> Unit
) {
    BoxWithConstraints {
        val screenHeight = maxHeight * 3 / 4

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
        ) {
            TranslationTextField(
                modifier = Modifier
                    .height(screenHeight),
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
                onCopyClick = onCopyClick,
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