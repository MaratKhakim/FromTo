package io.fromto.presentation.translation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.fromto.presentation.components.ShimmerPlaceholderForText
import io.fromto.presentation.theme.Dimens

@Composable
fun TranslateTextField(
    modifier: Modifier = Modifier,
    language: String,
    text: String,
    placeholder: String,
    readOnly: Boolean,
    isTranslating: Boolean = false,
    onValueChange: (String) -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
    headerContent: (@Composable () -> Unit)? = null,
    actionContent: (@Composable () -> Unit)? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var measuredLineCount by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .shadow(
                elevation = Dimens.Elevation,
                shape = MaterialTheme.shapes.extraLarge
            )
            .background(MaterialTheme.colorScheme.surface)
            .clip(MaterialTheme.shapes.large)
            .padding(Dimens.PaddingMedium)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text(
                    text = language,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.weight(1f)
                )
                headerContent?.invoke()
            }

            BasicTextField(
                value = text,
                onValueChange = onValueChange,
                readOnly = readOnly,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { onFocusChanged(it.isFocused) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                onTextLayout = { textLayoutResult ->
                    measuredLineCount = textLayoutResult.lineCount
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        keyboardController?.hide()
                        onFocusChanged(false)
                    }
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                decorationBox = { innerTextField ->
                    if (isTranslating) {
                        ShimmerPlaceholderForText(fromText = text)
                    } else {
                        if (text.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )

            if (text.isNotBlank()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    actionContent?.invoke()
                }
            }
        }
    }
}
