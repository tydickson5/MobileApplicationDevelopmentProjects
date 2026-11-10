package edu.virginia.cs4720.caravyn.Auth.data

import edu.virginia.cs4720.caravyn.Data.Local.entities.Profile
import kotlinx.coroutines.flow.Flow

class AuthRepository(private val db: AppDb, private val api: AuthApi) {

    fun observeProfile(): Flow<Profile> {}

    fun updateUsername() {}



}