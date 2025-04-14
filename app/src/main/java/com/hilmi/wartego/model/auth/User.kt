package com.hilmi.wartego.model.auth

import com.hilmi.wartego.model.profile.Address
import com.hilmi.wartego.model.profile.Card
import com.hilmi.wartego.model.profile.Cart

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val bio: String = "",
    val imageUrl: String = "",
    val address: List<Address> = emptyList(),
    val card: List<Card> = emptyList(),
    val cart: List<Cart> = emptyList()
)