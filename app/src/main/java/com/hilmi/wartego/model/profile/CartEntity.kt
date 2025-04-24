package com.hilmi.wartego.model.profile

data class CartEntity(
    val idProduct: String = "",
    val nameProduct: String = "",
    val imageUrl: String = "",
    val price: String = "",
    val quantity: Int = 0,
    var idEditable: Boolean = false
)
