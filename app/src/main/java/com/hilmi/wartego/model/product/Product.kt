package com.hilmi.wartego.model.product

data class Product(
    val id: String = "",
    val nameProduct: String = "",
    val imageUrl: String = "",
    val idRestaurant: String = "",
    val idCategory: Int = 0,
    val price: String = "",
    val ingredients: List<Ingredients> = emptyList()
)