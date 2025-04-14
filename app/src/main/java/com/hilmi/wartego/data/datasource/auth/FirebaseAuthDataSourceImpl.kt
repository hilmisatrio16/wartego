package com.hilmi.wartego.data.datasource.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hilmi.wartego.model.auth.User
import com.hilmi.wartego.model.response.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : FirebaseAuthDataSource {
    override suspend fun login(email: String, password: String): Flow<Response<AuthResult>> {
        return flow {
            try {
                emit(Response.Loading)
                val data = auth.signInWithEmailAndPassword(email, password).await()
                emit(Response.Success(data))
            } catch (e: Exception) {
                emit(Response.Error(e.localizedMessage ?: "Oops, login failed."))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Flow<Response<AuthResult>> {
        return flow {
            try {
                emit(Response.Loading)
                val data = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = data.user?.uid

                uid?.let {
                    firestore.collection("users")
                        .document(it)
                        .set(User(uid = it, fullName = fullName, email = email))
                        .await()
                }

                emit(Response.Success(data))
            } catch (e: Exception) {
                emit(Response.Error(e.localizedMessage ?: "Oops, register failed."))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun userUid(): String = auth.currentUser?.uid ?: ""

    override suspend fun isLoggedIn(): Boolean = auth.currentUser == null

    override suspend fun logout() = auth.signOut()
}