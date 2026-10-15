package edu.virginia.cs4720.collegefootballscores

data class Selection(val division: Division, val week: Int)

sealed interface UiState {
    data object Loading : UiState
    data class Success(
        val games: List<GameEntity>,
        val isRefreshing: Boolean = false,
        val notice: String? = null
    ) : UiState
    data class Error(val message: String) : UiState
}