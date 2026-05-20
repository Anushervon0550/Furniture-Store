package com.example.furniturestore.data.remote

import com.example.furniturestore.data.model.Category
import com.example.furniturestore.data.model.Order
import com.example.furniturestore.data.model.Product
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FurnitureApiService {
    @GET("/categories")
    suspend fun getCategories(): List<Category>

    @GET("/products")
    suspend fun getProducts(@Query("categoryId") categoryId: String? = null, @Query("query") query: String? = null): List<Product>

    @GET("/products/{id}")
    suspend fun getProduct(@Path("id") id: String): Product

    @GET("/orders")
    suspend fun getOrders(@Query("userId") userId: String): List<Order>

    @POST("/orders")
    suspend fun placeOrder(@Body order: Order): Order
}
