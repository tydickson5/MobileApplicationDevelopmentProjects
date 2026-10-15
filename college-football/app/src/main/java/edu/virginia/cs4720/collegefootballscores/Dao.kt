package edu.virginia.cs4720.collegefootballscores

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao{

    @Query("SELECT * FROM games WHERE division = :division AND season = :season AND week = :week ORDER BY startTimeEpoch")
    fun observeWeek(division: String, season: Int, week: Int): Flow<List<GameEntity>>

    @Query("DELETE FROM games WHERE division = :division AND season = :season AND week = :week")
    suspend fun deleteWeek(division: String, season: Int, week: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameEntity>)
}
