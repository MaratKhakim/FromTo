package io.fromto.presentation.translation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fromto.composeapp.generated.resources.Res
import fromto.composeapp.generated.resources.search
import fromto.composeapp.generated.resources.translate_from
import fromto.composeapp.generated.resources.translate_to
import io.fromto.analytics.LocalAnalytics
import io.fromto.analytics.ScreenName
import io.fromto.analytics.itemSelect
import io.fromto.domain.model.Language
import io.fromto.presentation.theme.Dimens
import io.fromto.presentation.util.getLanguageResource
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LanguagesListBottomSheet(
    modifier: Modifier,
    isSelectingSource: Boolean,
    onSourceLanguageSelected: (Language) -> Unit,
    onTargetLanguageSelected: (Language) -> Unit,
    onDismissRequest: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val analytics = LocalAnalytics.current

    var searchQuery by remember { mutableStateOf("") }
    val filteredLanguages = remember(searchQuery) {
        Language.entries.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    ModalBottomSheet(
        onDismissRequest = {
            searchQuery = ""
            onDismissRequest()
        },
        sheetState = bottomSheetState,
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    ) {
        Column(Modifier.padding(horizontal = Dimens.PaddingMedium)) {
            CenteredTextWithCloseButton(
                text = if (isSelectingSource) stringResource(Res.string.translate_from) else stringResource(
                    Res.string.translate_to
                ),
                onClose = {
                    coroutineScope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            searchQuery = ""
                            onDismissRequest()
                        }
                    }
                }
            )

            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.PaddingSmall),
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = stringResource(Res.string.search),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = stringResource(Res.string.search)
                    )
                },
                shape = MaterialTheme.shapes.large,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
            )

            LazyColumn {
                itemsIndexed(filteredLanguages) { index, language ->
                    Text(
                        text = stringResource(getLanguageResource(language.name)),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isSelectingSource) {
                                    onSourceLanguageSelected(language)
                                } else {
                                    onTargetLanguageSelected(language)
                                }
                                searchQuery = ""
                                analytics.itemSelect(
                                    screen = ScreenName.TRANSLATE,
                                    category = if (isSelectingSource) "source_language" else "target_language",
                                    itemId = language.name,
                                    itemName = language.name,
                                )
                                coroutineScope.launch { bottomSheetState.hide() }
                                    .invokeOnCompletion {
                                        if (!bottomSheetState.isVisible) {
                                            onDismissRequest()
                                        }
                                    }
                            }
                            .padding(
                                vertical = Dimens.PaddingMedium,
                                horizontal = Dimens.PaddingSmall
                            )
                    )

                    if (index < Language.entries.lastIndex) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.2f)
                        )
                    }
                }
            }
        }
    }
}