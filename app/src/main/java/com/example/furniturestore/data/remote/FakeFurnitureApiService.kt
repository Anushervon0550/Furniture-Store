package com.example.furniturestore.data.remote

import com.example.furniturestore.data.model.Category
import com.example.furniturestore.data.model.Order
import com.example.furniturestore.data.model.Product
import kotlinx.coroutines.delay

class FakeFurnitureApiService : FurnitureApiService {

    private val categories = listOf(
        Category(id = "sofas", name = "Диваны", iconUrl = "https://picsum.photos/seed/sofas/64/64"),
        Category(id = "tables", name = "Столы", iconUrl = "https://picsum.photos/seed/tables/64/64"),
        Category(id = "chairs", name = "Стулья", iconUrl = "https://picsum.photos/seed/chairs/64/64"),
        Category(id = "beds", name = "Кровати", iconUrl = "https://picsum.photos/seed/beds/64/64"),
        Category(id = "wardrobes", name = "Шкафы", iconUrl = "https://picsum.photos/seed/wardrobes/64/64")
    )

    private val allProducts: List<Product> = buildList {
        val seeds = listOf("oak", "walnut", "pine", "maple", "mahogany", "beech", "elm")
        var idx = 1
        fun p(cat: String, title: String, price: Double, popular: Boolean): Product {
            val seed = seeds[idx % seeds.size]
            val id = "p$idx"
            idx++
            val main = "https://picsum.photos/seed/${cat}-${seed}/600/400"
            val gallery = listOf(
                main,
                "https://picsum.photos/seed/${cat}-${seed}-2/600/400",
                "https://picsum.photos/seed/${cat}-${seed}-3/600/400"
            )
            return Product(
                id = id,
                title = title,
                description = "Эргономичная современная мебель высокой прочности. Подходит для любого интерьера.",
                price = price,
                rating = (4..5).random() + listOf(0.0, 0.3, 0.5, 0.7, 0.9).random(),
                imageUrl = main,
                images = gallery,
                categoryId = cat,
                isPopular = popular
            )
        }
        addAll(
            listOf(
                p("sofas", "Диван Stockholm", 599.99, true),
                p("sofas", "Диван Oslo", 749.99, true),
                p("tables", "Стол Berlin", 299.99, true),
                p("chairs", "Стул Paris", 129.99, true),
                p("beds", "Кровать Tokyo", 899.99, true),
                p("wardrobes", "Шкаф London", 659.99, true)
            )
        )
        addAll(
            listOf(
                p("sofas", "Диван Milano", 689.99, false),
                p("tables", "Стол Vienna", 349.99, false),
                p("chairs", "Стул Prague", 109.99, false),
                p("beds", "Кровать Zurich", 959.99, false),
                p("wardrobes", "Шкаф Madrid", 699.99, false)
            )
        )
    }

    private val placedOrders = mutableListOf<Order>()

    override suspend fun getCategories(): List<Category> {
        delay(300)
        return categories
    }

    override suspend fun getProducts(categoryId: String?, query: String?): List<Product> {
        delay(500)
        var res = allProducts
        if (!categoryId.isNullOrBlank()) res = res.filter { it.categoryId == categoryId }
        if (!query.isNullOrBlank()) res = res.filter { it.title.contains(query, ignoreCase = true) }
        return res
    }

    override suspend fun getProduct(id: String): Product {
        delay(300)
        return allProducts.first { it.id == id }
    }

    override suspend fun getOrders(userId: String): List<Order> {
        delay(250)
        return placedOrders
            .asSequence()
            .filter { it.userId == userId }
            .sortedByDescending { it.createdAt }
            .toList()
    }

    override suspend fun placeOrder(order: Order): Order {
        delay(500)
        val normalized = order.copy(
            id = if (order.id.isBlank()) "ord-" + System.currentTimeMillis() else order.id,
            createdAt = if (order.createdAt <= 0L) System.currentTimeMillis() else order.createdAt
        )
        placedOrders.add(normalized)
        return normalized
    }
}
