package io.fromto.presentation.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import io.fromto.domain.model.AppLocale
import io.fromto.presentation.theme.Dimens

@Composable
fun LocaleDropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSelectLocale: (AppLocale) -> Unit,
    activeLocale: AppLocale
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onExpandedChange(false) },
        shape = MaterialTheme.shapes.large,
        containerColor = MaterialTheme.colorScheme.surface,
        shadowElevation = Dimens.Elevation,
        border = BorderStroke(
            width = 0.5.dp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
        )
    ) {
        AppLocale.entries.forEach { locale ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = locale.languageLabel,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                onClick = { onSelectLocale(locale) },
                colors = MenuDefaults.itemColors().copy(
                    textColor = if (locale == activeLocale) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                ),
                contentPadding = PaddingValues(horizontal = Dimens.PaddingLarge)
            )
        }
    }
}