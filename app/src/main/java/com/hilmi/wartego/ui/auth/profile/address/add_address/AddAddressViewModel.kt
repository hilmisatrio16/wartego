package com.hilmi.wartego.ui.auth.profile.address.add_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmi.wartego.data.repositories.auth.FirebaseAuthRepository
import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.request.UpdateUser
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(private val repository: FirebaseAuthRepository) :
    ViewModel() {
    private val _addOnSuccess = MutableStateFlow<Response<Boolean>?>(null)
    val addOnSuccess = _addOnSuccess.asStateFlow()

    fun addAddress(address: Address) = viewModelScope.launch {
        repository.userUid().let { uid ->
            repository.addAddress(uid, address).collect {
                _addOnSuccess.emit(it)
            }

        }
    }
}