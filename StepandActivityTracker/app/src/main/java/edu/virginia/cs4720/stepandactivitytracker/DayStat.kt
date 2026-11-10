package edu.virginia.cs4720.stepandactivitytracker

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "day_stats")
data class DayStat (
    @PrimaryKey val date: String,
    val steps: Int,
    val sensorBaseline: Int
)

