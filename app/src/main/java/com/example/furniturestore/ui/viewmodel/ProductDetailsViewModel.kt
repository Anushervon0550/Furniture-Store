package com.example.furniturestore.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furniturestore.data.local.entity.FavoriteEntity
import com.example.furniturestore.data.model.Product
import com.example.furniturestore.data.repository.FurnitureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val repository: FurnitureRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    data class UiState(
        val loading: Boolean = true,
        val error: String? = null,
        val product: Product? = null,
        val quantity: Int = 1,
        val isFavorite: Boolean = false
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val productId: String = checkNotNull(savedStateHandle["productId"]) { "productId is required" }

    init {
        viewModelScope.launch {
            try {
                val product = repository.getProduct(productId)
                val fav = repository.isFavorite(productId)
                _state.emit(UiState(loading = false, product = product, isFavorite = fav))
            } catch (e: Exception) {
                _state.emit(UiState(loading = false, error = e.message ?: "Ошибка"))
            }
        }
    }

    fun increase() { _state.value = _state.value.copy(quantity = _state.value.quantity + 1) }
    fun decrease() { _state.value = _state.value.copy(quantity = (_state.value.quantity - 1).coerceAtLeast(1)) }

    fun toggleFavorite() {
        val p = _state.value.product ?: return
        viewModelScope.launch {
            val nowFav = _state.value.isFavorite
            if (nowFav) {
                repository.removeFromFavorites(FavoriteEntity(productId = p.id, title = p.title, price = p.price, imageUrl = p.imageUrl))
            } else {
                repository.addToFavorites(FavoriteEntity(productId = p.id, title = p.title, price = p.price, imageUrl = p.imageUrl))
            }
            _state.emit(_state.value.copy(isFavorite = !nowFav))
        }
    }
}
