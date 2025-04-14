package com.hilmi.wartego.model.profile

data class Card(
    val cardHolderName: String = "",
    val cardNumber: String = "",
    val expiredData: String = "",
    val cvc: String = ""
)
