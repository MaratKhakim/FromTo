package io.fromto.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fromto.composeapp.generated.resources.Res
import fromto.composeapp.generated.resources.text_copied
import io.fromto.presentation.history.HistoryEvent
import io.fromto.presentation.history.HistoryScreen
import io.fromto.presentation.history.HistoryViewModel
import io.fromto.presentation.navigation.BottomNavGraph
import io.fromto.presentation.navigation.Route
import io.fromto.presentation.navigation.getRoute
import io.fromto.presentation.navigation.navigateWithPopUp
import io.fromto.presentation.translation.TranslateEvent
import io.fromto.presentation.translation.TranslateViewModel
import io.fromto.presentation.translation.TranslationScreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val textCopiedConfirmation = stringResource(Res.string.text_copied)

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val translateViewModel = koinViewModel<TranslateViewModel>()
    val translateState by translateViewModel.state.collectAsState()

    val historyViewModel = koinViewModel<HistoryViewModel>()
    val historyState by historyViewModel.state.collectAsState()

    var isLocalizationMenuOpen by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AdaptiveTopBar(
                activeLocale = translateState.locale,
                expanded = isLocalizationMenuOpen,
                onExpandedChange = {
                    isLocalizationMenuOpen = it
                },
                destination = currentDestination?.getRoute() ?: Route.Translate,
                onClickLocalization = {
                    isLocalizationMenuOpen = true
                },
                onSelectLocale = {
                    isLocalizationMenuOpen = false
                    translateViewModel.onEvent(TranslateEvent.SelectLocale(it))
                },
                onClearHistory = {
                    if (historyState.items.isNotEmpty()) {
                        showConfirmDialog = true
                    }
                }
            )
        },
        bottomBar = {
            BottomNavGraph(
                currentDestination = currentDestination,
                onRouteChange = {
                    navController.navigateWithPopUp(it)
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Route.Translate
        ) {
            composable<Route.Translate> {
                TranslationScreen(
                    state = translateState,
                    onEvent = translateViewModel::onEvent,
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
            }
            composable<Route.History> {
                HistoryScreen(
                    state = historyState,
                    onEvent = historyViewModel::onEvent,
                    onBack = {
                        navController.navigateWithPopUp(Route.Translate)
                    },
                    onItemClicked = {
                        translateViewModel.onEvent(
                            TranslateEvent.SelectHistoryItem(
                                sourceText = it.sourceText,
                                translatedText = it.translatedText,
                                sourceLang = it.sourceLang,
                                targetLang = it.targetLang
                            )
                        )
                        navController.navigateWithPopUp(Route.Translate)
                    }
                )
            }
        }

        if (showConfirmDialog) {
            DeleteConfirmationDialog(
                onConfirm = {
                    historyViewModel.onEvent(HistoryEvent.DeleteAllItems)
                    showConfirmDialog = false
                },
                onDismiss = { showConfirmDialog = false }
            )
        }
    }
}
