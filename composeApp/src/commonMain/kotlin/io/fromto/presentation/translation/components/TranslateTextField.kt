package io.fromto.presentation.translation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import fromto.composeapp.generated.resources.Res
import fromto.composeapp.generated.resources.copy
import fromto.composeapp.generated.resources.enter_text
import fromto.composeapp.generated.resources.new_translation
import io.fromto.presentation.shimmerEffect
import io.fromto.presentation.theme.Dimens
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TranslationTextField(
    modifier: Modifier = Modifier,
    inputText: String,
    translatedText: String,
    fromLanguage: String,
    toLanguage: String,
    isTranslating: Boolean,
    onCopyClick: (String) -> Unit,
    onCloseClick: () -> Unit,
    onTextChange: (String) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .shadow(
                elevation = Dimens.Elevation,
                shape = MaterialTheme.shapes.extraLarge
            )
            .background(MaterialTheme.colorScheme.background)
            .clip(MaterialTheme.shapes.large)
            .padding(Dimens.PaddingSmall)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                focusRequester.requestFocus()
            }
    ) {
        LazyColumn {
            item {
                if (inputText.isNotBlank() && translatedText.isNotBlank() && !isFocused) {
                    LanguageTitle(
                        modifier = Modifier.padding(
                            start = Dimens.PaddingMedium,
                            top = Dimens.PaddingMedium,
                        ),
                        language = fromLanguage,
                    )
                }
            }
            item {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = onTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { isFocused = it.isFocused },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.enter_text),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Go
                    ),
                    supportingText = {
                        val isNotBlank = inputText.isNotBlank() && translatedText.isNotBlank()
                        if (isNotBlank || isTranslating) {
                            Column {
                                if (isNotBlank) {
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.onSurface.copy(
                                            0.2f
                                        )
                                    )
                                }
                                if (!isFocused) {
                                    LanguageTitle(
                                        modifier = Modifier.padding(top = Dimens.PaddingMedium),
                                        language = toLanguage,
                                    )
                                }
                                TranslatedTextSection(
                                    modifier = Modifier.padding(top = Dimens.PaddingMedium),
                                    isTranslating = isTranslating,
                                    fromText = inputText,
                                    translatedText = translatedText
                                )
                                if (!isTranslating && isNotBlank) {
                                    IconButton(
                                        onClick = { onCopyClick(translatedText) }
                                    ) {
                                        Icon(
                                            painter = painterResource(Res.drawable.copy),
                                            contentDescription = stringResource(Res.string.copy),
                                            tint = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }
                            }
                        }
                    },
                    keyboardActions = KeyboardActions {
                        keyboardManager.clearFocus()
                        focusRequester.freeFocus()
                    }
                )
            }
        }
        if (inputText.isNotBlank())
            Button(
                modifier = Modifier.align(Alignment.BottomEnd),
                shape = MaterialTheme.shapes.large,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.secondary,
                ),
                onClick = {
                    focusRequester.requestFocus()
                    onCloseClick()
                }
            ) {
                Text(
                    text = stringResource(Res.string.new_translation),
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
    }
}

@Composable
fun TranslatedTextSection(
    modifier: Modifier,
    fromText: String,
    translatedText: String,
    isTranslating: Boolean
) {
    if (isTranslating) {
        Text(
            text = fromText,
            modifier = modifier
                .shimmerEffect(true)
                .alpha(0f)
        )
    }

    Text(
        text = translatedText,
        modifier = modifier.alpha(if (isTranslating) 0f else 1f),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.bodyMedium
    )
}


@Composable
fun LanguageTitle(
    modifier: Modifier = Modifier,
    language: String
) {
    Text(
        modifier = modifier,
        text = language,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold
    )
}