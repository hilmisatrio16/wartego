package com.hilmi.wartego.model.product

data class Restaurant(
    val id: String,
    val nameRestaurant: String,
    val desc: String,
    val category: List<String>
)