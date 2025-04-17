package com.hilmi.wartego.model.product

data class Restaurant(
    val id: String = "",
    val nameRestaurant: String = "",
    val imageUrl: String = "",
    val desc: String = "",
    val address: String = "",
    val pointReview: String = "",
    val category: List<String> = emptyList()
)