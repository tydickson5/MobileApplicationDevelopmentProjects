
package edu.virginia.cs4720.collegefootballscores

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

//from AI
fun currentWeek(nowMillis: Long = System.currentTimeMillis()): Int {
    val et = TimeZone.getTimeZone("America/New_York")
    val start = Calendar.getInstance(et).apply { clear(); set(2026, Calendar.AUGUST, 31) }.timeInMillis
    val days = Math.floorDiv(nowMillis - start, 86_400_000L)
    return (Math.floorDiv(days, 7L).toInt() + 1).coerceIn(1, 14)
}

class BucketViewModel(app: Application

) : AndroidViewModel(app) {

    val season = 2026

    private val api = GamesAPI.create()
    private val repository = BucketRepository(AppDb.getInstance(app), api)

    private val _selection = MutableStateFlow(Selection(Division.FBS, currentWeek()))
    val selection: StateFlow<Selection> = _selection

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init { load() }

    fun selectWeek(week: Int) {
        _selection.value = _selection.value.copy(week = week)
        load()
    }

    fun selectDivision(division: Division) {
        _selection.value = _selection.value.copy(division = division)
        load()
    }

    fun load(){
        viewModelScope.launch {
            val division = _selection.value.division
            val week = _selection.value.week

            //show whatever is saved right away
            val saved = repository.observeWeek(division, season, week).first()
            _uiState.value =
                if (saved.isEmpty()) UiState.Loading
                else UiState.Success(saved, isRefreshing = true, notice = null)

            // fetch new data
            val result = repository.refreshDb(division, season, week)

            //read new data
            val newData = repository.observeWeek(division, season, week).first()

            _uiState.value = when {
                newData.isNotEmpty() -> UiState.Success(
                    newData,
                    isRefreshing = false,
                    notice = if (result.isFailure) "Offline, showing saved scores" else null
                )
                result.isFailure -> UiState.Error("Couldn't load scores. ${result.exceptionOrNull()}")
                else -> UiState.Success(emptyList())
            }
        }
    }

}
