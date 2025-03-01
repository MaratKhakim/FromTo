package io.fromto.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import fromto.composeapp.generated.resources.Res
import fromto.composeapp.generated.resources.app_name
import fromto.composeapp.generated.resources.history
import fromto.composeapp.generated.resources.text_copied
import fromto.composeapp.generated.resources.translate
import io.fromto.presentation.history.HistoryScreen
import io.fromto.presentation.translation.TranslateViewModel
import io.fromto.presentation.translation.TranslationScreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val textCopiedConfirmation = stringResource(Res.string.text_copied)
    var currentDestination by remember { mutableStateOf(Destination.Translate) }
    val viewModel = koinViewModel<TranslateViewModel>()
    val state by viewModel.state.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AdaptiveTopBar(
                destination = currentDestination,
                onLocalizationSwitch = {
                    println("Localization switch clicked")
                },
                onClearHistory = {
                    println("Clear history clicked")
                }
            )
        },
        bottomBar = {
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
                Destination.entries.toTypedArray().forEach { destination ->
                    NavigationBarItem(
                        icon = { Icon(painterResource(destination.icon), null) },
                        label = { Text(stringResource(destination.label)) },
                        selected = currentDestination == destination,
                        onClick = { currentDestination = destination },
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
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentDestination) {
                Destination.Translate ->
                    TranslationScreen(
                        state = state,
                        onEvent = viewModel::onEvent,
                        onCopyClick = {
                            clipboardManager.setText(
                                buildAnnotatedString {
                                    append(it)
                                }
                            )
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(textCopiedConfirmation)
                            }
                        }
                    )

                Destination.History -> HistoryScreen()
            }
        }
    }
}

enum class Destination(
    val title: StringResource,
    val label: StringResource,
    val icon: DrawableResource,
) {
    Translate(
        title = Res.string.app_name,
        label = Res.string.translate,
        icon = Res.drawable.translate
    ),
    History(
        title = Res.string.history,
        label = Res.string.history,
        icon = Res.drawable.history
    )
}
