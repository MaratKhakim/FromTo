package io.fromto.presentation.translation

import LanguageSelector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.fromto.presentation.theme.Dimens

@Composable
fun TranslationScreen() {
    var inputText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }
    var isTranslating by remember { mutableStateOf(false) }

    Scaffold { padding ->
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
                    inputText = inputText,
                    translatedText = translatedText,
                    isTranslating = isTranslating,
                    fromLanguage = "English",
                    toLanguage = "Spanish",
                    onCloseClick = {
                        inputText = ""
                        translatedText = ""
                        isTranslating = false
                    },
                    onTextChange = {
                        inputText = it
                        translatedText = "Translate: $it"
                    },
                    onCopyClick = {},
                )

                LanguageSelector(
                    sourceLanguage = "English",
                    targetLanguage = "Spanish",
                    onSourceLanguageSelected = {},
                    onTargetLanguageSelected = {}
                )
            }
        }
    }
}