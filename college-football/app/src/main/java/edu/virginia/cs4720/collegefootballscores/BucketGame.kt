package edu.virginia.cs4720.collegefootballscores

import android.telecom.Conference
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class ScoreboardDto(
    val games: List<GameWrapperDto> = emptyList()
)

@Serializable
data class GameWrapperDto(
    val game: GameDto
)

@Serializable
data class GameDto(
    val gameID: String,
    val away: TeamDto,
    val home: TeamDto,
    val startTime: String = "",
    val startTimeEpoch: String = "",
    val gameState: String = "",
    val finalMessage: String = "",
    val currentPeriod: String = "",
    val contestClock: String = ""
)

@Serializable
data class TeamDto(
    val score: String = "",
    val names: NamesDto = NamesDto(),
    val winner: Boolean = false,
    val rank: String = "",
    val conferences: List<ConferenceDto> = emptyList()
)

@Serializable
data class NamesDto(val short: String = "", val seo: String = "")
@Serializable data class ConferenceDto(val conferenceSeo: String = "")
data class Team(
    val name: String,
    val seo: String,
    val winner: Boolean,
    val score: Int?,
    val rank: Int?,
    val conference: String,
)

@Entity(tableName = "games", primaryKeys = ["gameId", "division"])
data class GameEntity(
    val gameId: String,
    val division: String,
    val season: Int,
    val week: Int,
    val startTime: String,
    val startTimeEpoch: Long,
    val gameState: String,
    val finalMessage: String,
    val currentPeriod: String,
    val contestClock: String,
    @Embedded(prefix = "away_") val away: Team,
    @Embedded(prefix = "home_") val home: Team
)

enum class Division(val path: String, val label: String) {
    FBS("fbs", "FBS"), FCS("fcs", "FCS")
}

//mapping
private fun TeamDto.toSide() = Team(
    name = names.short,
    seo = names.seo,
    score = score.toIntOrNull(),
    rank = rank.toIntOrNull(),
    winner = winner,
    conference = conferences.firstOrNull()?.conferenceSeo ?: ""
)

fun GameDto.toEntity(division: Division, season: Int, week: Int) = GameEntity(
    gameId = gameID,
    division = division.path,
    season = season,
    week = week,
    startTime = startTime,
    startTimeEpoch = startTimeEpoch.toLongOrNull() ?: 0L,
    gameState = gameState,
    finalMessage = finalMessage,
    currentPeriod = currentPeriod,
    contestClock = contestClock,
    away = away.toSide(),
    home = home.toSide()
)


enum class GameStatus { UPCOMING, LIVE, FINAL }

fun GameEntity.status(): GameStatus = when {
    gameState.equals("final", ignoreCase = true) -> GameStatus.FINAL
    gameState.equals("live", ignoreCase = true) -> GameStatus.LIVE
    else -> GameStatus.UPCOMING
}