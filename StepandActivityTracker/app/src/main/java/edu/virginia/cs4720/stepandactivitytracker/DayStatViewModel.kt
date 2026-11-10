package edu.virginia.cs4720.stepandactivitytracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DayStatViewModel(app: Application): AndroidViewModel(app) {

    private val repository = DayStatRepository(AppDb.getInstance(app))

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)

    val uiState: StateFlow<UIState> = _uiState

    fun load() {
        viewModelScope.launch {
            repository.observeAll().collect { saved ->

                _uiState.value =
                    if (saved.isEmpty()) {
                        UIState.Success(emptyList())
                    } else {
                        UIState.Success(saved)
                    }
            }
        }
    }



}
