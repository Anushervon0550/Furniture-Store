package com.example.furniturestore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furniturestore.data.local.entity.CartEntity
import com.example.furniturestore.data.model.Product
import com.example.furniturestore.data.model.CartItem
import com.example.furniturestore.data.model.Order
import com.example.furniturestore.data.repository.FurnitureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: FurnitureRepository
) : ViewModel() {

    data class UiState(
        val items: List<CartEntity> = emptyList(),
        val total: Double = 0.0,
        val loading: Boolean = false,
        val error: String? = null
    )

    val state: StateFlow<UiState> = repository.observeCart()
        .map { list ->
            val total = list.sumOf { it.price * it.quantity }
            UiState(items = list, total = total)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState(loading = true))

    private val _placing = MutableStateFlow(false)
    val placing: StateFlow<Boolean> = _placing.asStateFlow()

    private val _orderId = MutableStateFlow<String?>(null)
    val orderId: StateFlow<String?> = _orderId.asStateFlow()

    private val _orderError = MutableStateFlow<String?>(null)
    val orderError: StateFlow<String?> = _orderError.asStateFlow()

    fun add(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            val safeQuantity = quantity.coerceAtLeast(1)
            val existing = repository.observeCart().first().firstOrNull { it.productId == product.id }

            if (existing != null) {
                repository.updateCart(existing.copy(quantity = existing.quantity + safeQuantity))
            } else {
                val entity = CartEntity(
                    productId = product.id,
                    title = product.title,
                    price = product.price,
                    imageUrl = product.imageUrl,
                    quantity = safeQuantity
                )
                repository.addToCart(entity)
            }
        }
    }

    fun increase(item: CartEntity) {
        viewModelScope.launch {
            repository.updateCart(item.copy(quantity = item.quantity + 1))
        }
    }

    fun decrease(item: CartEntity) {
        viewModelScope.launch {
            val q = (item.quantity - 1).coerceAtLeast(1)
            repository.updateCart(item.copy(quantity = q))
        }
    }

    fun remove(item: CartEntity) {
        viewModelScope.launch { repository.removeFromCart(item) }
    }

    fun checkout() {
        viewModelScope.launch {
            try {
                _placing.emit(true)
                _orderError.emit(null)
                _orderId.emit(null)

                val itemsEntities = repository.observeCart().first()
                if (itemsEntities.isEmpty()) {
                    _orderError.emit("Корзина пуста")
                    return@launch
                }

                val items = itemsEntities.map { e ->
                    val p = repository.getProduct(e.productId)
                    CartItem(product = p, quantity = e.quantity)
                }
                val total = items.sumOf { it.product.price * it.quantity }
                val order = Order(id = "", userId = "guest", items = items, totalAmount = total)
                val placed = repository.placeOrder(order)
                repository.clearCart()
                _orderId.emit(placed.id)
            } catch (e: Exception) {
                _orderError.emit(e.message ?: "Ошибка оформления")
            } finally {
                _placing.emit(false)
            }
        }
    }
}
