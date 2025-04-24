package com.hilmi.wartego.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmi.wartego.data.repositories.auth.FirebaseAuthRepository
import com.hilmi.wartego.data.repositories.dashboard.FirebaseProductRepository
import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.profile.Cart
import com.hilmi.wartego.model.profile.CartEntity
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repositoryProduct: FirebaseProductRepository,
    private val repositoryAuth: FirebaseAuthRepository
): ViewModel() {
    private val _cart = MutableStateFlow<Response<List<CartEntity>>>(Response.Loading)
    val cart = _cart.asStateFlow()

    private val _editable = MutableStateFlow<Boolean>(false)
    val editable = _editable.asStateFlow()

    val dataCart = mutableListOf<CartEntity>()
    init {
        getCart()
    }

    fun addDataCart(data: List<CartEntity>){
        dataCart.clear()
        dataCart.addAll(data)
    }
    fun isEdit() = viewModelScope.launch {
        _editable.emit(!_editable.value)
    }
    fun getCart() = viewModelScope.launch {
        repositoryAuth.userUid().let { uid ->
            repositoryProduct.cart(uid).collect {
                _cart.emit(it)
            }

        }
    }
}