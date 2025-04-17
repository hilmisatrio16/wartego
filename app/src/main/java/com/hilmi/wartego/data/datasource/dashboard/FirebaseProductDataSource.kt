package com.hilmi.wartego.data.datasource.dashboard

import com.google.firebase.auth.AuthResult
import com.hilmi.wartego.model.product.Product
import com.hilmi.wartego.model.product.Restaurant
import com.hilmi.wartego.model.profile.Card
import com.hilmi.wartego.model.profile.Cart
import com.hilmi.wartego.model.response.Response
import kotlinx.coroutines.flow.Flow

interface FirebaseProductDataSource {
//    suspend fun addCard(data: Card): Flow<Response<AuthResult>>
//    suspend fun addCart(data: Cart): Flow<Response<AuthResult>>
//    suspend fun deleteCart(): Flow<Response<AuthResult>>
//    suspend fun detailRestaurant(): Flow<Response<Restaurant>>

    suspend fun foods(category: Int): Flow<Response<List<Product>>>
    suspend fun category(): Flow<Response<List<com.hilmi.wartego.model.product.Category>>>
    suspend fun restaurants(): Flow<Response<List<Restaurant>>>
    suspend fun detailRestaurant(id: String): Flow<Response<Restaurant>>
}