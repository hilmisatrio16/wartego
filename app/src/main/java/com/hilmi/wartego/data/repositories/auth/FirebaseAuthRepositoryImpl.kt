package com.hilmi.wartego.data.repositories.auth

import com.google.firebase.auth.AuthResult
import com.hilmi.wartego.data.datasource.auth.FirebaseAuthDataSource
import com.hilmi.wartego.model.auth.User
import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.request.UpdateUser
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

    override suspend fun user(uid: String): Flow<Response<User>> {
        return firebaseAuthDataSource.user(uid)
    }

    override suspend fun updateUser(uid: String, user: UpdateUser): Boolean {
        return firebaseAuthDataSource.updateUser(uid, user)
    }

    override suspend fun addAddress(uid: String, address: Address): Flow<Response<Boolean>> {
        return firebaseAuthDataSource.addAddress(uid, address)
    }

    override suspend fun address(uid: String): Flow<Response<List<Address>>> {
        return firebaseAuthDataSource.address(uid)
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