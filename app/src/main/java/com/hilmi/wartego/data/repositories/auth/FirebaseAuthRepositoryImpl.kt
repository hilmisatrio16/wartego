package com.hilmi.wartego.data.repositories.auth

import com.google.firebase.auth.AuthResult
import com.hilmi.wartego.data.datasource.auth.FirebaseAuthDataSource
import com.hilmi.wartego.model.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : FirebaseAuthRepository {
    override suspend fun login(email: String, password: String): Flow<Response<AuthResult>> {
        return firebaseAuthDataSource.login(email, password)
    }

    override suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Flow<Response<AuthResult>> {
        return firebaseAuthDataSource.register(email, password, fullName)
    }

    override suspend fun userUid(): String {
        return firebaseAuthDataSource.userUid()
    }

    override suspend fun isLoggedIn(): Boolean {
        return firebaseAuthDataSource.isLoggedIn()
    }

    override suspend fun logout() {
        return firebaseAuthDataSource.logout()
    }
}