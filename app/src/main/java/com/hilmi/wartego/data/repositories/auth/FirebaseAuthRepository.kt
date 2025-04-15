package com.hilmi.wartego.data.repositories.auth

import com.google.firebase.auth.AuthResult
import com.hilmi.wartego.model.auth.User
import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.request.UpdateUser
import com.hilmi.wartego.model.response.Response
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    suspend fun login(email: String, password: String): Flow<Response<AuthResult>>
    suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Flow<Response<AuthResult>>
    suspend fun user(uid: String): Flow<Response<User>>
    suspend fun updateUser(uid: String, user: UpdateUser): Boolean
    suspend fun addAddress(uid: String, address: Address): Flow<Response<Boolean>>
    suspend fun address(uid: String): Flow<Response<List<Address>>>
    suspend fun userUid(): String
    suspend fun isLoggedIn(): Boolean
    suspend fun logout()
}