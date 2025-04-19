package com.hilmi.wartego.data.repositories.dashboard

import com.hilmi.wartego.model.product.Category
import com.hilmi.wartego.model.product.Product
import com.hilmi.wartego.model.product.Restaurant
import com.hilmi.wartego.model.profile.Cart
import com.hilmi.wartego.model.response.Response
import kotlinx.coroutines.flow.Flow

interface FirebaseProductRepository {
    suspend fun foods(category: Int): Flow<Response<List<Product>>>
    suspend fun detailFood(id: String): Flow<Response<Product>>
    suspend fun foodsByRestaurant(idRestaurant: String,category: Int): Flow<Response<List<Product>>>
    suspend fun category(): Flow<Response<List<Category>>>
    suspend fun restaurants(): Flow<Response<List<Restaurant>>>
    suspend fun detailRestaurant(id: String): Flow<Response<Restaurant>>
    suspend fun addCart(uid: String, cart: Cart): Flow<Response<Boolean>>
    suspend fun cart(uid: String): Flow<Response<List<Cart>>>
}