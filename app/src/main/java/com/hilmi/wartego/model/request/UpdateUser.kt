package com.hilmi.wartego.model.request

data class UpdateUser(
    val fullName: String,
    val phoneNumber: String,
    val bio: String
)