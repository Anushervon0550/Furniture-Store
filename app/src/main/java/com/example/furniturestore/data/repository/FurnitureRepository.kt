package com.example.furniturestore.data.repository

import com.example.furniturestore.data.local.entity.CartEntity
import com.example.furniturestore.data.local.entity.FavoriteEntity
import com.example.furniturestore.data.model.Category
import com.example.furniturestore.data.model.Order
import com.example.furniturestore.data.model.Product
import kotlinx.coroutines.flow.Flow

interface FurnitureRepository {
    // Remote
    suspend fun getCategories(): List<Category>
    suspend fun getProducts(categoryId: String? = null, query: String? = null): List<Product>
    suspend fun getProduct(id: String): Product
    suspend fun getOrders(userId: String): List<Order>
    suspend fun placeOrder(order: Order): Order

    // Local - Cart
    fun observeCart(): Flow<List<CartEntity>>
    suspend fun addToCart(entity: CartEntity)
    suspend fun updateCart(entity: CartEntity)
    suspend fun removeFromCart(entity: CartEntity)
    suspend fun clearCart()

    // Local - Favorites
    fun observeFavorites(): Flow<List<FavoriteEntity>>
    suspend fun addToFavorites(entity: FavoriteEntity)
    suspend fun removeFromFavorites(entity: FavoriteEntity)
    suspend fun isFavorite(productId: String): Boolean
}
