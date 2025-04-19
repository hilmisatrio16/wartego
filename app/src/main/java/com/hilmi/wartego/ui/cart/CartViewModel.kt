package com.hilmi.wartego.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmi.wartego.data.repositories.auth.FirebaseAuthRepository
import com.hilmi.wartego.data.repositories.dashboard.FirebaseProductRepository
import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.profile.Cart
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
    private val _cart = MutableStateFlow<Response<List<Cart>>>(Response.Loading)
    val cart = _cart.asStateFlow()

    init {
        getCart()
    }

    fun getCart() = viewModelScope.launch {
        repositoryAuth.userUid().let { uid ->
            repositoryProduct.cart(uid).collect {
                _cart.emit(it)
            }

        }
    }
}