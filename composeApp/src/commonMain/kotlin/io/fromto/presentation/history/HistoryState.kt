package io.fromto.presentation.history

import io.fromto.domain.model.HistoryItem
import io.fromto.domain.util.HistoryError

data class HistoryState(
    val items: List<HistoryItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: HistoryError? = null,
)