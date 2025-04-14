package com.hilmi.wartego.data.datasource.auth

import com.google.firebase.auth.AuthResult
import com.hilmi.wartego.model.response.Response
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthDataSource {
    suspend fun login(email: String, password: String): Flow<Response<AuthResult>>
    suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Flow<Response<AuthResult>>

    suspend fun userUid(): String
    suspend fun isLoggedIn(): Boolean
    suspend fun logout()
}