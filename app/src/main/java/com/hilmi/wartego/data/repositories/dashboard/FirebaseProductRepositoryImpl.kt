package com.hilmi.wartego.data.repositories.dashboard

import com.hilmi.wartego.data.datasource.dashboard.FirebaseProductDataSource
import com.hilmi.wartego.model.product.Category
import com.hilmi.wartego.model.product.Product
import com.hilmi.wartego.model.product.Restaurant
import com.hilmi.wartego.model.profile.Cart
import com.hilmi.wartego.model.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirebaseProductRepositoryImpl @Inject constructor(
    private val firebaseProductDataSource: FirebaseProductDataSource
) : FirebaseProductRepository {
    override suspend fun foods(category: Int): Flow<Response<List<Product>>> {
        return firebaseProductDataSource.foods(category)
    }

    override suspend fun detailFood(id: String): Flow<Response<Product>> {
        return firebaseProductDataSource.detailFood(id)
    }

    override suspend fun foodsByRestaurant(
        idRestaurant: String,
        category: Int
    ): Flow<Response<List<Product>>> {
        return firebaseProductDataSource.foodsByRestaurant(idRestaurant, category)
    }

    override suspend fun category(): Flow<Response<List<Category>>> {
        return firebaseProductDataSource.category()
    }

    override suspend fun restaurants(): Flow<Response<List<Restaurant>>> {
        return firebaseProductDataSource.restaurants()
    }

    override suspend fun detailRestaurant(id: String): Flow<Response<Restaurant>> {
        return firebaseProductDataSource.detailRestaurant(id)
    }

    override suspend fun addCart(uid: String, cart: Cart): Flow<Response<Boolean>> {
        return firebaseProductDataSource.addCart(uid, cart)
    }

    override suspend fun cart(uid: String): Flow<Response<List<Cart>>> {
        return firebaseProductDataSource.cart(uid)
    }
}