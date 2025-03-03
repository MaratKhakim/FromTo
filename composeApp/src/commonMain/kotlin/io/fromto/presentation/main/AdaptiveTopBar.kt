package io.fromto.presentation.main

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import fromto.composeapp.generated.resources.Res
import fromto.composeapp.generated.resources.change_language
import fromto.composeapp.generated.resources.clear_history
import fromto.composeapp.generated.resources.switch_localization
import fromto.composeapp.generated.resources.trash
import io.fromto.domain.model.AppLocale
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveTopBar(
    activeLocale: AppLocale,
    expanded: Boolean,
    destination: Destination,
    onExpandedChange: (Boolean) -> Unit,
    onSelectLocale: (AppLocale) -> Unit,
    onClickLocalization: () -> Unit,
    onClearHistory: () -> Unit
) {
    val actions = getTopBarActionsForRoute(destination, onClickLocalization, onClearHistory)

    TopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        title = {
            Text(
                text = stringResource(destination.title),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        actions = {
            actions.forEach { action ->
                IconButton(onClick = action.onClick) {
                    Icon(
                        painter = painterResource(action.icon),
                        contentDescription = stringResource(action.contentDescription)
                    )
                }
            }
            LocaleDropdownMenu(expanded, onExpandedChange, onSelectLocale, activeLocale)
        }
    )
}

fun getTopBarActionsForRoute(
    destination: Destination,
    onLocalizationSwitch: () -> Unit,
    onClearHistory: () -> Unit
): List<TopBarAction> {
    return when (destination) {
        Destination.Translate -> listOf(
            TopBarAction(
                icon = Res.drawable.change_language,
                contentDescription = Res.string.switch_localization,
                onClick = onLocalizationSwitch
            )
        )

        Destination.History -> listOf(
            TopBarAction(
                icon = Res.drawable.trash,
                contentDescription = Res.string.clear_history,
                onClick = onClearHistory
            )
        )
    }
}

