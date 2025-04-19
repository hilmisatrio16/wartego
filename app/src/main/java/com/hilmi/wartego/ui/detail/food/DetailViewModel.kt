package com.hilmi.wartego.ui.detail.food

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmi.wartego.data.repositories.auth.FirebaseAuthRepository
import com.hilmi.wartego.data.repositories.dashboard.FirebaseProductRepository
import com.hilmi.wartego.model.product.Product
import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.profile.Cart
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: FirebaseProductRepository,
    private val authRepository: FirebaseAuthRepository
) :
    ViewModel() {
    private val _food = MutableStateFlow<Response<Product>>(Response.Loading)
    val food = _food.asStateFlow()

    private val _foods = MutableStateFlow<Response<List<Product>>>(Response.Loading)
    val foods = _foods.asStateFlow()

    private val _quantity = MutableStateFlow<Int>(1)
    val quantity = _quantity.asStateFlow()

    private val _addCartSuccess = MutableStateFlow<Response<Boolean>?>(null)
    val addCartSuccess = _addCartSuccess.asStateFlow()


    fun addQuantity() = viewModelScope.launch {
        val quantity = _quantity.value + 1
        Log.d("QUANTITY", quantity.toString())
        _quantity.emit(quantity)
    }

    fun minQuantity() = viewModelScope.launch {
        val quantity = _quantity.value
        if (quantity > 1) {
            _quantity.emit(quantity - 1)
        }

    }


    fun addCart(cart: Cart) = viewModelScope.launch {
        authRepository.userUid().let { uid ->
            repository.addCart(uid, cart).collect {
                _addCartSuccess.emit(it)
                Log.d("CART FOOD", it.toString() + "" + uid)
            }

        }
    }

    fun getFoods(idCategory: Int) = viewModelScope.launch {
        repository.foods(idCategory).collect {
            _foods.emit(it)
        }
    }

    fun getFood(id: String) = viewModelScope.launch {
        repository.detailFood(id).collect {
            _food.emit(it)
        }
    }
}