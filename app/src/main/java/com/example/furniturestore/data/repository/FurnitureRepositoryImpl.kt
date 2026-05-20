package com.example.furniturestore.data.repository

import com.example.furniturestore.data.local.AppDatabase
import com.example.furniturestore.data.local.entity.CartEntity
import com.example.furniturestore.data.local.entity.FavoriteEntity
import com.example.furniturestore.data.model.Category
import com.example.furniturestore.data.model.Order
import com.example.furniturestore.data.model.Product
import com.example.furniturestore.data.remote.FurnitureApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FurnitureRepositoryImpl @Inject constructor(
    private val api: FurnitureApiService,
    private val db: AppDatabase
) : FurnitureRepository {

    // Remote
    override suspend fun getCategories(): List<Category> = api.getCategories()

    override suspend fun getProducts(categoryId: String?, query: String?): List<Product> =
        api.getProducts(categoryId, query)

    override suspend fun getProduct(id: String): Product = api.getProduct(id)

    override suspend fun getOrders(userId: String): List<Order> = api.getOrders(userId)

    override suspend fun placeOrder(order: Order): Order = api.placeOrder(order)

    // Local - Cart
    override fun observeCart(): Flow<List<CartEntity>> = db.cartDao().observeCart()

    override suspend fun addToCart(entity: CartEntity) = db.cartDao().upsert(entity)

    override suspend fun updateCart(entity: CartEntity) = db.cartDao().update(entity)

    override suspend fun removeFromCart(entity: CartEntity) = db.cartDao().delete(entity)

    override suspend fun clearCart() = db.cartDao().clear()

    // Local - Favorites
    override fun observeFavorites(): Flow<List<FavoriteEntity>> = db.favoriteDao().observeFavorites()

    override suspend fun addToFavorites(entity: FavoriteEntity) = db.favoriteDao().addToFavorites(entity)

    override suspend fun removeFromFavorites(entity: FavoriteEntity) = db.favoriteDao().removeFromFavorites(entity)

    override suspend fun isFavorite(productId: String): Boolean = db.favoriteDao().isFavorite(productId)
}
