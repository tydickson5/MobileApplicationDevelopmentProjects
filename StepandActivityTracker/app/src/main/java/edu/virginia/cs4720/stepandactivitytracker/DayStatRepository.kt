package edu.virginia.cs4720.stepandactivitytracker

import kotlinx.coroutines.flow.Flow

class DayStatRepository(private val db: AppDb) {

    fun observeAll(): Flow<List<DayStat>> {
        return db.bucketDao().observeDayStats()
    }
}
