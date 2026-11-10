package edu.virginia.cs4720.caravyn.Auth.data

import edu.virginia.cs4720.caravyn.Data.Local.entities.ProfileDto

interface AuthApi {

    @GET("users/")
    suspend fun getUserProfile(

    ): ProfileDto
}