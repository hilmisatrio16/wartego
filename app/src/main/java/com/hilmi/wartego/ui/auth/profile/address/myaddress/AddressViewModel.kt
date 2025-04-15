package com.hilmi.wartego.ui.auth.profile.address.myaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmi.wartego.data.repositories.auth.FirebaseAuthRepository
import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(private val repository: FirebaseAuthRepository) :
    ViewModel() {
    private val _address = MutableStateFlow<Response<List<Address>>>(Response.Loading)
    val address = _address.asStateFlow()

    init {
        getAddresses()
    }

    fun getAddresses() = viewModelScope.launch {
        repository.userUid().let { uid ->
            repository.address(uid).collect {
                _address.emit(it)
            }

        }
    }
}