package com.hilmi.wartego.ui.splashscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmi.wartego.data.repositories.auth.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val repository: FirebaseAuthRepository
) : ViewModel() {
    private val _isLogin = MutableStateFlow<Boolean?>(null)
    val isLogin = _isLogin.asStateFlow()

    init {
        checkLoginStatus()
    }

    fun checkLoginStatus() = viewModelScope.launch {
        _isLogin.emit(repository.isLoggedIn())
    }
}