package com.hilmi.wartego.data.datasource.dashboard

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hilmi.wartego.model.product.Category
import com.hilmi.wartego.model.product.Product
import com.hilmi.wartego.model.product.Restaurant
import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.profile.Cart
import com.hilmi.wartego.model.response.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

class FirebaseProductDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirebaseProductDataSource {
    override suspend fun foods(category: Int): Flow<Response<List<Product>>> {
        return flow {
            try {
                emit(Response.Loading)
                val data = firestore.collection("food")
                    .whereEqualTo("idCategory", category)
                    .get()
                    .await()
                if (!data.isEmpty) {
                    val response = data.toObjects(Product::class.java)
                    Log.d("DEBUG FOOD", response.toString())
                    emit(Response.Success(response))
                } else {
                    emit(Response.Error("Error"))
                }

            } catch (e: Exception) {
                emit(Response.Error("Error"))
                Log.d("DEBUG FOOD", e.message.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun detailFood(id: String): Flow<Response<Product>> {
        return flow {
            try {
                emit(Response.Loading)
                val data = firestore.collection("food")
                    .document(id)
                    .get()
                    .await()
                if (data.exists()) {
                    val response = data.toObject(Product::class.java)
                    if (response != null) {
                        emit(Response.Success(response))
                    }
                } else {
                    emit(Response.Error("Error"))
                }

            } catch (e: Exception) {
                emit(Response.Error("Error"))
                Log.d("DEBUG FOOD", e.message.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun foodsByRestaurant(
        idRestaurant: String,
        category: Int
    ): Flow<Response<List<Product>>> {
        return flow {
            try {
                emit(Response.Loading)
                val data = firestore.collection("food")
                    .whereEqualTo("idCategory", category)
                    .whereEqualTo("idRestaurant", idRestaurant)
                    .get()
                    .await()
                if (!data.isEmpty) {
                    val response = data.toObjects(Product::class.java)
                    emit(Response.Success(response))
                } else {
                    emit(Response.Error("Error"))
                }

            } catch (e: Exception) {
                emit(Response.Error("Error"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun category(): Flow<Response<List<Category>>> {
        return flow {
            try {
                emit(Response.Loading)
                val data = firestore.collection("category")
                    .get()
                    .await()
                if (!data.isEmpty) {
                    val response = data.toObjects(Category::class.java)
                    emit(Response.Success(response))
                } else {
                    emit(Response.Error("Error"))
                }

            } catch (e: Exception) {
                emit(Response.Error("Error"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun restaurants(): Flow<Response<List<Restaurant>>> {
        return flow {
            try {
                emit(Response.Loading)
                val data = firestore.collection("restaurant")
                    .get()
                    .await()
                if (!data.isEmpty) {
                    val response = data.toObjects(Restaurant::class.java)
                    Log.d("DEBUG RESTAURANT", response.toString())
                    emit(Response.Success(response))
                } else {
                    emit(Response.Error("Error"))
                }

            } catch (e: Exception) {
                emit(Response.Error("Error"))
                Log.d("DEBUG RESTAURANT", e.message.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun detailRestaurant(id: String): Flow<Response<Restaurant>> {
        return flow {
            try {
                emit(Response.Loading)
                val data = firestore.collection("restaurant")
                    .document(id)
                    .get()
                    .await()

                if (data.exists()) {
                    val response = data.toObject(Restaurant::class.java)
                    if (response != null) {
                        emit(Response.Success(response))
                    }
                } else {
                    emit(Response.Error("Error"))
                }

            } catch (e: Exception) {
                emit(Response.Error("Error"))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addCart(uid: String, cart: Cart): Flow<Response<Boolean>> {
        return flow {
            try {
                emit(Response.Loading)
                firestore.collection("users")
                    .document(uid)
                    .update("cart", FieldValue.arrayUnion(cart))
                    .await()
                emit(Response.Success(true))
            } catch (e: Exception) {
                emit(Response.Success(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun cart(uid: String): Flow<Response<List<Cart>>> {
        return flow {
            try {
                emit(Response.Loading)
                val data = firestore.collection("users")
                    .document(uid)
                    .get()
                    .await()

                val listData = data.get("cart") as? List<Map<String, Any>>

                val cartList = listData?.map { addressMap ->
                    Cart(
                        idProduct = addressMap["idProduct"] as? String ?: "",
                        nameProduct = addressMap["nameProduct"] as? String ?: "",
                        imageUrl = addressMap["imageUrl"] as? String ?: "",
                        price = addressMap["price"] as? String ?: "",
                        quantity = addressMap["quantity"] as? Int ?: 0,
                    )
                }

                if (cartList != null) {
                    emit(Response.Success(cartList))
                } else {
                    emit(Response.Error("Not found"))
                }

            } catch (e: Exception) {
                emit(Response.Error("Error"))
            }
        }.flowOn(Dispatchers.IO)
    }
}