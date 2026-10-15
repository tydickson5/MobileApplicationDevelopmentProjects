package edu.virginia.cs4720.collegefootballscores

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val eastern = TimeZone.getTimeZone("America/New_York")
private val dayFmt = SimpleDateFormat("EEEE, MMM d", Locale.US).apply { timeZone = eastern }
private val timeFmt = SimpleDateFormat("EEE h:mm a 'ET'", Locale.US).apply { timeZone = eastern }

@Composable
fun ScoreboardScreen(vm: BucketViewModel = viewModel()){

    //help from AI on how to represent these in the screen
    val state by vm.uiState.collectAsStateWithLifecycle()
    val selection by vm.selection.collectAsStateWithLifecycle()
    val loading = state is UiState.Loading || (state as? UiState.Success)?.isRefreshing == true



    Column() {
        Row(
            Modifier.fillMaxWidth().padding(3.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("College Football Scoreboard")
            Button(
                onClick = { vm.load() },
                enabled = !loading
            ){
                Text("Refresh")
            }
        }

        //Ai showed me how to add this type of button
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Division.entries.forEachIndexed { i, d ->
                SegmentedButton(
                    selected = selection.division == d,
                    onClick = { vm.selectDivision(d) },
                    shape = SegmentedButtonDefaults.itemShape(i, Division.entries.size)
                ) { Text(d.label) }
            }
        }

        WeekSelector(selected = selection.week, onSelect = vm::selectWeek)

        if (loading) LinearProgressIndicator(Modifier.fillMaxWidth())

        when(val s = state ){
            UiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

            is UiState.Error -> Column(
                Modifier.fillMaxSize().padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Couldn't load scores",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
                Text(s.message, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = vm::load) { Text("Retry") }
            }
            is UiState.Success -> Column() {
                if (s.games.isEmpty()) {
                    Text("There are not games this week")
                } else {
                    val byDay = remember(s.games) {
                        s.games.groupBy { dayFmt.format(Date(it.startTimeEpoch * 1000)) }
                    }

                    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)) {
                        byDay.forEach { (day, games) ->
                            item(key = "header_$day") {
                                Text(
                                    day,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(start = 8.dp, top = 12.dp, bottom = 4.dp)
                                )
                            }
                            items(games, key = { "${it.gameId}_${it.division}" }) { GameCard(it) }
                        }
                    }
                }

            }

        }

    }

}

@Composable
private fun WeekSelector(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = { onSelect(selected - 1) }, enabled = selected > 1) { Text("◀ Prev") }
        Text("Week $selected", style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = { onSelect(selected + 1) }, enabled = selected < 14) { Text("Next ▶") }
    }
}

fun livePeriodLabel(period: String, clock: String): String {
    val quarter = when (period.uppercase()) {
        "1ST" -> "1st Quarter"
        "2ND" -> "2nd Quarter"
        "3RD" -> "3rd Quarter"
        "4TH" -> "4th Quarter"
        else -> return period.ifBlank { "In progress" }
    }
    var c = clock.trimStart('0')
    if (c.startsWith(":")) c = "0$c"
    return if (c.isBlank()) quarter else "$quarter · $c"
}

fun finalLabel(msg: String): String {
    val m = msg.trim()
    return when {
        m.isBlank() || m.equals("FINAL", ignoreCase = true) -> "Final"
        m.startsWith("FINAL", ignoreCase = true) -> "Final" + m.drop(5)
        else -> m
    }
}

//Used AI just to help with styling if the game cards
@Composable
private fun GameCard(g: GameEntity) {
    val status = g.status()
    val showScores = status != GameStatus.UPCOMING

    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(12.dp)) {
            TeamRow(g.away, "AWAY", showScores, status == GameStatus.FINAL)
            Spacer(Modifier.height(4.dp))
            TeamRow(g.home, "HOME", showScores, status == GameStatus.FINAL)
            Spacer(Modifier.height(8.dp))
            Text(
                text = when (status) {
                    GameStatus.UPCOMING ->
                        if (g.startTimeEpoch > 0) timeFmt.format(Date(g.startTimeEpoch * 1000))
                        else g.startTime
                    GameStatus.LIVE -> "LIVE · " + livePeriodLabel(g.currentPeriod, g.contestClock)
                    GameStatus.FINAL -> finalLabel(g.finalMessage)
                },
                style = MaterialTheme.typography.labelLarge,
                color = if (status == GameStatus.LIVE) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun TeamRow(team: Team, role: String, showScore: Boolean, isFinal: Boolean) {
    val won = isFinal && team.winner
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            role,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(44.dp)
        )
        Text(
            (team.rank?.let { "#$it " } ?: "") + team.name,
            fontWeight = if (won) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        if (showScore) {
            Text(
                team.score?.toString() ?: "–",
                fontWeight = if (won) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
