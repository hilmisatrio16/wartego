package com.hilmi.wartego.ui.auth.login

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
class LoginViewModel @Inject constructor(
    private val repository: FirebaseAuthRepository
) : ViewModel() {
    private val _loginUser = MutableSharedFlow<Response<AuthResult>>()
    val loginUser = _loginUser

    fun login(email: String, password: String) = viewModelScope.launch {
        repository.login(email, password).collect { response ->
            _loginUser.emit(response)
        }
    }
}