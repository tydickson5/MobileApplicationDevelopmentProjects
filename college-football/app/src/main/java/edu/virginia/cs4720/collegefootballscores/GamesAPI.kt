package edu.virginia.cs4720.collegefootballscores

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import kotlin.jvm.java


interface GamesAPI{

    @GET("scoreboard/football/{division}/{season}/{week}")
    suspend fun getScoreboard(
        @Path("division") division: String,
        @Path("season") season: Int,
        @Path("week") week: String
    ): ScoreboardDto

    companion object { //AI helped me build this function
        fun create(): GamesAPI {
            val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
            return Retrofit.Builder()
                .baseUrl("https://ncaa-api.henrygd.me/")
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(GamesAPI::class.java)
        }
    }
}