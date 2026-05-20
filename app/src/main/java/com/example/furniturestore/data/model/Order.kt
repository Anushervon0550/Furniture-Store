package com.example.furniturestore.data.model

import androidx.annotation.Keep

@Keep
data class Order(
    val id: String,
    val userId: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val createdAt: Long = System.currentTimeMillis()
)
