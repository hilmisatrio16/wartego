package com.hilmi.wartego.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmi.wartego.data.repositories.dashboard.FirebaseProductRepository
import com.hilmi.wartego.model.product.Product
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: FirebaseProductRepository) :
    ViewModel() {
    private val _foods = MutableStateFlow<Response<List<Product>>>(Response.Loading)
    val foods = _foods.asStateFlow()

    fun getFoods(idCategory: Int) = viewModelScope.launch {
        repository.foods(idCategory).collect {
            _foods.emit(it)
        }
    }
}