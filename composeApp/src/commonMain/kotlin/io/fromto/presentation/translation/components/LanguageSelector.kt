package io.fromto.presentation.translation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fromto.composeapp.generated.resources.Res
import fromto.composeapp.generated.resources.swap
import fromto.composeapp.generated.resources.swap_horiz_white
import io.fromto.analytics.Buttons
import io.fromto.analytics.LocalAnalytics
import io.fromto.analytics.ScreenName
import io.fromto.analytics.buttonClick
import io.fromto.domain.model.Language
import io.fromto.presentation.theme.Dimens
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LanguageSelector(
    sourceLanguage: String,
    targetLanguage: String,
    onSourceLanguageSelected: (Language) -> Unit,
    onTargetLanguageSelected: (Language) -> Unit,
    onSwapLanguages: () -> Unit,
    modifier: Modifier = Modifier
) {
    val analytics = LocalAnalytics.current

    var isSelectingSource by remember { mutableStateOf(true) }
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        LanguagesListBottomSheet(
            modifier,
            isSelectingSource,
            onSourceLanguageSelected,
            onTargetLanguageSelected,
            onDismissRequest = {
                showBottomSheet = false
            }
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.PaddingMedium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LanguageButton(
            modifier = Modifier.weight(1f),
            text = sourceLanguage
        ) {
            isSelectingSource = true
            showBottomSheet = true
            analytics.buttonClick(
                screen = ScreenName.TRANSLATE,
                button = Buttons.SOURCE_LANGUAGE
            )
        }

        IconButton(
            onClick = {
                onSwapLanguages()
                analytics.buttonClick(
                    screen = ScreenName.TRANSLATE,
                    button = Buttons.SWAP_LANGUAGES
                )
            }
        ) {
            Icon(
                painter = painterResource(Res.drawable.swap_horiz_white),
                contentDescription = stringResource(Res.string.swap),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        LanguageButton(
            modifier = Modifier.weight(1f),
            text = targetLanguage
        ) {
            isSelectingSource = false
            showBottomSheet = true
            analytics.buttonClick(
                screen = ScreenName.TRANSLATE,
                button = Buttons.TARGET_LANGUAGE
            )
        }
    }
}
