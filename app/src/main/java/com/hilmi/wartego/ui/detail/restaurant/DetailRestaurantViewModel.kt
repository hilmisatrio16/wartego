package com.hilmi.wartego.ui.detail.restaurant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmi.wartego.data.repositories.dashboard.FirebaseProductRepository
import com.hilmi.wartego.model.product.Category
import com.hilmi.wartego.model.product.Restaurant
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailRestaurantViewModel @Inject constructor(private val repository: FirebaseProductRepository) :
    ViewModel() {
    private val _detailRestaurant = MutableStateFlow<Response<Restaurant>>(Response.Loading)
    val detailRestaurant = _detailRestaurant.asStateFlow()

    fun getDetailRestaurant(id:String) = viewModelScope.launch {
        repository.detailRestaurant(id).collect {
            _detailRestaurant.emit(it)
        }
    }
}