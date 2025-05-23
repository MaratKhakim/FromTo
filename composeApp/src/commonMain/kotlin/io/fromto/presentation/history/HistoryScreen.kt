package io.fromto.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import fromto.composeapp.generated.resources.Res
import fromto.composeapp.generated.resources.error_delete_error
import io.fromto.analytics.LocalAnalytics
import io.fromto.analytics.ScreenName
import io.fromto.analytics.itemSelect
import io.fromto.domain.model.HistoryItem
import io.fromto.domain.model.Language
import io.fromto.domain.util.HistoryError
import io.fromto.presentation.components.ErrorMessage
import io.fromto.presentation.components.Loading
import io.fromto.presentation.components.SwipeToDismissItem
import io.fromto.presentation.components.TrackScreenViewEvent
import io.fromto.presentation.theme.Dimens
import io.fromto.presentation.util.getLanguageResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HistoryScreen(
    state: HistoryState,
    onEvent: (HistoryEvent) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onItemClicked: (HistoryItem) -> Unit
) {
    TrackScreenViewEvent(
        screenName = ScreenName.HISTORY,
        analytics = LocalAnalytics.current
    )
    when {
        state.isLoading -> Loading()
        state.items.isEmpty() -> EmptyHistoryComponent(onStartTranslating = onBack)
        else -> HistoryList(
            historyItems = state.items,
            onDelete = { id -> onEvent(HistoryEvent.DeleteItem(id)) },
            onItemClicked = onItemClicked,
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.PaddingMedium),
        )
    }
    state.error?.let {
        ErrorMessage(
            errorMessage = it.toMessage(),
            onDismiss = {
                onEvent(HistoryEvent.ClearError)
            }
        )
    }
}

@Composable
private fun HistoryList(
    historyItems: List<HistoryItem>,
    onDelete: (String) -> Unit,
    onItemClicked: (HistoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val analytics = LocalAnalytics.current
    LazyColumn(
        modifier = modifier,
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
    ) {
        items(historyItems) { item ->
            SwipeToDismissItem(
                modifier = Modifier
                    .shadow(elevation = Dimens.Elevation, shape = MaterialTheme.shapes.large)
                    .clip(MaterialTheme.shapes.large),
                item = item,
                onDismiss = onDelete
            ) {
                val sourceLang = Language.fromCode(item.sourceLang).name
                val targetLang = Language.fromCode(item.targetLang).name
                HistoryItemCard(
                    sourceText = item.sourceText,
                    translatedText = item.translatedText,
                    fromLanguage = stringResource(getLanguageResource(sourceLang)),
                    toLanguage = stringResource(getLanguageResource(targetLang)),
                    timestamp = item.timestamp,
                    onClick = {
                        analytics.itemSelect(
                            screen = ScreenName.HISTORY,
                            category = "history_item",
                            itemId = "${sourceLang}-${targetLang}",
                        )
                        onItemClicked(item)
                    }
                )
            }
        }
    }
}

@Composable
fun HistoryError.toMessage(): String {
    return when (this) {
        HistoryError.DeleteError -> stringResource(Res.string.error_delete_error)
    }
}
