package edu.virginia.cs4720.stepandactivitytracker

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DayStatDao{

    @Query("SELECT * FROM day_stats WHERE date = :date")
    suspend fun getDay(date: String): DayStat?

    @Query("SELECT * FROM day_stats ORDER BY date DESC")
    fun observeDayStats(): Flow<List<DayStat>>

    @Upsert
    suspend fun insertOrUpdate(dayStat: DayStat)
}