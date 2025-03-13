package io.fromto.presentation.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import io.fromto.domain.model.AppLocale
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavGraph(
    currentDestination: NavDestination?,
    locale: AppLocale,
    onRouteChange: (Route) -> Unit
) {
    val routes = listOf(Route.Translate, Route.History)

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                // Draw a line at the top
                drawLine(
                    color = Color.Black.copy(alpha = 0.2f),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        val defaultColors = NavigationBarItemDefaults.colors()
        routes.forEach { route ->
            NavigationBarItem(
                icon = { Icon(painterResource(route.icon), null) },
                label = { Label(stringResource(route.label), locale = locale) },
                selected = currentDestination?.hierarchy?.any {
                    it.getRoute() == route
                } == true,
                onClick = { onRouteChange(route) },
                colors = NavigationBarItemColors(
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledIconColor = defaultColors.disabledIconColor,
                    disabledTextColor = defaultColors.disabledTextColor,
                    selectedIndicatorColor = Color.Transparent,
                )
            )
        }
    }
}

/**
Bottom navigation label on iOS is not being updated correctly when locale is changed
TODO: Find a better way to do this
 */
@Composable
private fun Label(
    text: String,
    locale: AppLocale
) {
    Text(text = text)
}