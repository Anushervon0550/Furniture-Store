package com.example.furniturestore.data.model

import androidx.annotation.Keep

@Keep
data class Product(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val imageUrl: String,
    val images: List<String>,
    val categoryId: String,
    val isPopular: Boolean
)
