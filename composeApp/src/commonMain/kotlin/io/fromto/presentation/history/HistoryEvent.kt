package io.fromto.presentation.history

sealed interface HistoryEvent {
    data object ClearError : HistoryEvent
    data class DeleteItem(val id: String) : HistoryEvent
    data object DeleteAllItems : HistoryEvent
}
