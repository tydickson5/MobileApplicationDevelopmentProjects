package edu.virginia.cs4720.stepandactivitytracker

sealed interface UIState {

    data object Loading: UIState

    data class Success(
        val days: List<DayStat>,
        val isRefreshing: Boolean = false,
        val notice: String? = null
    ) : UIState

    data class Error(val message: String) : UIState
}
