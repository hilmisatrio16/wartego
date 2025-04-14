package com.hilmi.wartego.model.product

data class Product(
    val id: String,
    val nameProduct: String,
    val imageUrl: String,
    val idRestaurant: String,
    val price: String,
    val ingredients: List<Ingredients>
)