package com.hilmi.wartego.data.datasource.auth

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hilmi.wartego.model.auth.User
import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.request.UpdateUser
import com.hilmi.wartego.model.response.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import org.jetbrains.annotations.Debug
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

    override suspend fun user(uid: String): Flow<Response<User>> {
        return flow {
            try {
                emit(Response.Loading)
                val data = firestore.collection("users")
                    .document(uid)
                    .get()
                    .await()
                if (data.exists()) {
                    val user = data.toObject(User::class.java)
                    if (user != null) {
                        emit(Response.Success(user))
                    } else {
                        emit(Response.Error("User not found"))
                    }

                }

            } catch (e: Exception) {
                emit(Response.Error(e.localizedMessage ?: "Oops, user failed."))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateUser(uid: String, user: UpdateUser): Boolean {
        val mapUser = mapOf(
            "fullName" to user.fullName,
            "phoneNumber" to user.phoneNumber,
            "bio" to user.bio
        )
        return try {
            firestore.collection("users")
                .document(uid)
                .update(mapUser)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addAddress(uid: String, address: Address): Flow<Response<Boolean>> {
        return flow {
            try {
                emit(Response.Loading)
                firestore.collection("users")
                    .document(uid)
                    .update("address", FieldValue.arrayUnion(address))
                    .await()
                emit(Response.Success(true))
            } catch (e: Exception) {
                emit(Response.Success(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun address(uid: String): Flow<Response<List<Address>>> {
        return flow {
            try {
                emit(Response.Loading)
                val data = firestore.collection("users")
                    .document(uid)
                    .get()
                    .await()

                val addressListData = data.get("address") as? List<Map<String, Any>>

                val addressList = addressListData?.map { addressMap ->
                    Address(
                        address = addressMap["address"] as? String ?: "",
                        street = addressMap["street"] as? String ?: "",
                        postCode = addressMap["postCode"] as? Int ?: 0,
                        apartment = addressMap["apartment"] as? String ?: "",
                        label = addressMap["label"] as? String ?: "",
                    )
                }

                if (addressList != null) {
                    emit(Response.Success(addressList))
                } else {
                    emit(Response.Error("Not found"))
                }

            } catch (e: Exception) {
                emit(Response.Error("Error"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun userUid(): String = auth.currentUser?.uid ?: ""

    override suspend fun isLoggedIn(): Boolean = auth.currentUser != null

    override suspend fun logout() = auth.signOut()
}