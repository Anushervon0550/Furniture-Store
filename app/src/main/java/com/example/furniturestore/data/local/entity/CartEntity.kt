package com.example.furniturestore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: String,
    val title: String,
    val price: Double,
    val imageUrl: String,
    val quantity: Int
)
