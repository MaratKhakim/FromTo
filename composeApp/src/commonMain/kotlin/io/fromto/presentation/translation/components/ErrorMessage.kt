package io.fromto.presentation.translation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import fromto.composeapp.generated.resources.Res
import fromto.composeapp.generated.resources.dismiss_error
import fromto.composeapp.generated.resources.error
import fromto.composeapp.generated.resources.error_network_unavailable
import fromto.composeapp.generated.resources.error_serialization_error
import fromto.composeapp.generated.resources.error_server_error
import fromto.composeapp.generated.resources.error_timeout
import fromto.composeapp.generated.resources.error_too_many_requests
import fromto.composeapp.generated.resources.error_unknown
import io.fromto.domain.util.TranslateError
import io.fromto.presentation.theme.Dimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorMessage(
    error: TranslateError,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingSmall)
            .clickable { onDismiss() },
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.Elevation),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(Dimens.PaddingMedium),
            verticalAlignment = CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = stringResource(Res.string.error),
                modifier = Modifier.size(Dimens.IconSize)
            )

            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))

            Text(
                text = error.toMessage(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(Dimens.IconSize)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.dismiss_error)
                )
            }
        }
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

