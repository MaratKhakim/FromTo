package io.fromto.presentation.translation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fromto.composeapp.generated.resources.Res
import fromto.composeapp.generated.resources.close
import fromto.composeapp.generated.resources.copy
import fromto.composeapp.generated.resources.enter_text
import fromto.composeapp.generated.resources.error_network_unavailable
import fromto.composeapp.generated.resources.error_serialization_error
import fromto.composeapp.generated.resources.error_server_error
import fromto.composeapp.generated.resources.error_timeout
import fromto.composeapp.generated.resources.error_too_many_requests
import fromto.composeapp.generated.resources.error_unknown
import fromto.composeapp.generated.resources.translation_result
import io.fromto.domain.util.TranslateError
import io.fromto.presentation.components.ErrorMessage
import io.fromto.presentation.theme.Dimens
import io.fromto.presentation.translation.components.LanguageSelector
import io.fromto.presentation.translation.components.TranslateTextField
import io.fromto.presentation.util.getLanguageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TranslationScreen(
    state: TranslateState,
    onCopyClick: (String) -> Unit,
    onEvent: (TranslateEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
    ) {
        TranslateTextField(
            modifier = Modifier.weight(1f),
            language = stringResource(getLanguageResource(state.fromLanguage.name)),
            text = state.fromText,
            onValueChange = {
                onEvent(TranslateEvent.EnterText(it))
            },
            readOnly = false,
            placeholder = stringResource(Res.string.enter_text),
            onFocusChanged = {
                onEvent(TranslateEvent.SaveTranslation(it))
            },
            headerContent = {
                if (state.fromText.isNotBlank()) {
                    IconButton(
                        onClick = {
                            onEvent(TranslateEvent.ClearText)
                        },
                        modifier = Modifier.size(Dimens.IconSizeSmall)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(Res.string.close),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        )

        TranslateTextField(
            modifier = Modifier.weight(2f),
            language = stringResource(getLanguageResource(state.toLanguage.name)),
            text = if (state.isTranslating) state.fromText else state.toText,
            readOnly = true,
            isTranslating = state.isTranslating,
            placeholder = stringResource(Res.string.translation_result),
            actionContent = {
                IconButton(
                    onClick = {
                        onCopyClick(state.toText)
                    },
                    modifier = Modifier.size(Dimens.IconSizeMedium)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.copy),
                        contentDescription = stringResource(Res.string.copy),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        )

        LanguageSelector(
            sourceLanguage = stringResource(getLanguageResource(state.fromLanguage.name)),
            targetLanguage = stringResource(getLanguageResource(state.toLanguage.name)),
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
            errorMessage = it.toMessage(),
            onDismiss = {
                onEvent(TranslateEvent.ClearError)
            }
        )
    }
}

@Composable
fun TranslateError.toMessage(): String {
    return when (this) {
        TranslateError.NetworkUnavailable -> stringResource(Res.string.error_network_unavailable)
        TranslateError.ServerError -> stringResource(Res.string.error_server_error)
        TranslateError.Timeout -> stringResource(Res.string.error_timeout)
        TranslateError.TooManyRequests -> stringResource(Res.string.error_too_many_requests)
        TranslateError.SerializationError -> stringResource(Res.string.error_serialization_error)
        TranslateError.UnknownError -> stringResource(Res.string.error_unknown)
    }
}