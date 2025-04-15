package com.hilmi.wartego.ui.auth.profile.info.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmi.wartego.data.repositories.auth.FirebaseAuthRepository
import com.hilmi.wartego.model.auth.User
import com.hilmi.wartego.model.request.UpdateUser
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: FirebaseAuthRepository
) : ViewModel() {
    private val _user = MutableStateFlow<Response<User>>(Response.Loading)
    val user = _user.asStateFlow()

    private val _editOnSuccess = MutableStateFlow<Boolean?>(null)
    val editOnSuccess = _editOnSuccess.asStateFlow()

    init {
        getUser()
    }

    fun getUser() = viewModelScope.launch {
        repository.userUid().let { uid ->
            repository.user(uid).collect { response ->
                _user.emit(response)
            }
        }
    }

    fun updateUser(user: UpdateUser) = viewModelScope.launch {
        repository.userUid().let { uid ->
            val response = repository.updateUser(uid, user)
            _editOnSuccess.emit(response)
        }
    }
}