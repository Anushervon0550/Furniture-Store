package com.example.furniturestore.data.model

import androidx.annotation.Keep

@Keep
data class User(
    val id: String,
    val name: String,
    val email: String,
    val token: String? = null
)
