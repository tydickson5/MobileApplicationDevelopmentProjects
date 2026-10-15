package edu.virginia.cs4720.collegefootballscores

import androidx.room.Dao
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.cancellation.CancellationException

class BucketRepository(private val db: AppDb, private val api: GamesAPI) {

    fun observeWeek(division: Division, season: Int, week: Int): Flow<List<GameEntity>> {
        return db.bucketDao().observeWeek(division.path, season, week)
    }

    suspend fun refreshDb(division: Division, season: Int, week: Int): Result<Unit> {
        try {
            val dto = api.getScoreboard(division.path, season, "%02d".format(week))
            val entities = dto.games.map { it.game.toEntity(division, season, week) }
            db.withTransaction {
                db.bucketDao().deleteWeek(division.path, season, week)
                db.bucketDao().insertAll(entities)
            }
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}