package io.fromto.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.fromto.domain.usecase.ClearHistoryUseCase
import io.fromto.domain.usecase.DeleteHistoryUseCase
import io.fromto.domain.usecase.GetHistoryUseCase
import io.fromto.domain.util.HistoryError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val getHistory: GetHistoryUseCase,
    private val deleteHistory: DeleteHistoryUseCase,
    private val clearHistory: ClearHistoryUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HistoryState()
    )

    init {
        loadHistory()
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.DeleteItem -> deleteItem(event.id)
            is HistoryEvent.DeleteAllItems -> deleteAllItems()
            is HistoryEvent.ClearError -> _state.update { it.copy(error = null) }
        }
    }

    private fun loadHistory() {
        _state.update { it.copy(isLoading = true) }
        getHistory(viewModelScope.coroutineContext)
            .onEach { items ->
                _state.update { it.copy(items = items, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun deleteAllItems() {
        viewModelScope.launch {
            try {
                clearHistory()
            } catch (e: Exception) {
                _state.update { it.copy(error = HistoryError.DeleteError) }
            }
        }
    }

    private fun deleteItem(id: String) {
        viewModelScope.launch {
            try {
                deleteHistory(id)
            } catch (e: Exception) {
                _state.update { it.copy(error = HistoryError.DeleteError) }
            }
        }
    }
}
