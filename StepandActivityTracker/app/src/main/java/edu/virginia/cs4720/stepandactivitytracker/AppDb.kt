package edu.virginia.cs4720.stepandactivitytracker

import android.app.Application
import android.content.Context
import androidx.room3.Dao
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase


@Database(entities = [DayStat::class], version = 1 )
abstract class AppDb: RoomDatabase() {

    abstract  fun bucketDao(): DayStatDao

    companion object {
        @Volatile private var INSTANCE: AppDb? = null

        fun getInstance(context: Context): AppDb =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, AppDb::class.java, "day_stats.db")
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build().also { INSTANCE = it }
            }

    }


}