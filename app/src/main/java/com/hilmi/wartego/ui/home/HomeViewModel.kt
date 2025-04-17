package com.hilmi.wartego.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.hilmi.wartego.data.repositories.auth.FirebaseAuthRepository
import com.hilmi.wartego.data.repositories.dashboard.FirebaseProductRepository
import com.hilmi.wartego.model.auth.User
import com.hilmi.wartego.model.product.Category
import com.hilmi.wartego.model.product.Product
import com.hilmi.wartego.model.product.Restaurant
import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FirebaseProductRepository) :
    ViewModel() {

    private val _category = MutableStateFlow<Response<List<Category>>>(Response.Loading)
    val category = _category.asStateFlow()

    private val _restaurant = MutableStateFlow<Response<List<Restaurant>>>(Response.Loading)
    val restaurant = _restaurant.asStateFlow()

    init {
        getCategory()
        getRestaurants()
    }

    fun getCategory() = viewModelScope.launch {
        repository.category().collect {
            _category.emit(it)
        }
    }

    fun getRestaurants() = viewModelScope.launch {
        repository.restaurants().collect{
            _restaurant.emit(it)
        }
    }
}