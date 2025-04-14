package com.hilmi.wartego.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.hilmi.wartego.data.repositories.auth.FirebaseAuthRepository
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: FirebaseAuthRepository
) : ViewModel() {
    private val _registerUser = MutableSharedFlow<Response<AuthResult>>()
    val registerUser = _registerUser

    fun register(email: String, password: String, fullName: String) = viewModelScope.launch {
        repository.register(email, password, fullName).collect { response ->
            _registerUser.emit(response)
        }
    }
}