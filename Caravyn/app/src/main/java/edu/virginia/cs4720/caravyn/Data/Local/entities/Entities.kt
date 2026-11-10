package edu.virginia.cs4720.caravyn.Data.Local.entities

import androidx.room3.Entity
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Entity("profiles", primaryKeys = ["id"])
data class Profile(
    val id: String,
    val username: String,
    val created_at: String
)

@Entity("posts", primaryKeys = ["id"])
data class Post(
    val id: String,
    val user_id: String,
    val caption: String,
    val media_url: String,
    val media_type: String,
    val longitutde: Double,
    val latitude: Double,
    val created_at: String
)

@Serializable
data class ProfileDto(
    val id: String,
    val username: String,
    val created_at: String
)