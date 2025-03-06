package io.fromto.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import fromto.composeapp.generated.resources.Res
import fromto.composeapp.generated.resources.empty_history_button
import fromto.composeapp.generated.resources.empty_history_title
import io.fromto.presentation.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmptyHistoryComponent(
    modifier: Modifier = Modifier,
    onStartTranslating: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize()
            .padding(Dimens.PaddingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(Dimens.ImageSize)
                .padding(bottom = Dimens.PaddingLarge)
        )
        Text(
            text = stringResource(Res.string.empty_history_title),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Dimens.PaddingLarge)
        )

        FilledTonalButton(
            onClick = onStartTranslating,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                modifier = Modifier.size(Dimens.IconSizeSmall)
            )
            Spacer(Modifier.width(Dimens.PaddingSmall))
            Text(
                text = stringResource(Res.string.empty_history_button),
            )
        }
    }
}
