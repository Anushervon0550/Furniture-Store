package com.example.furniturestore.data.model

import androidx.annotation.Keep

@Keep
data class CartItem(
    val product: Product,
    val quantity: Int
)
