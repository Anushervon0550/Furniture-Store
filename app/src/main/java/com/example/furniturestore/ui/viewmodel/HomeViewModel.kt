package com.example.furniturestore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furniturestore.data.model.Category
import com.example.furniturestore.data.model.Product
import com.example.furniturestore.data.repository.FurnitureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FurnitureRepository
) : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val error: String? = null,
        val categories: List<Category> = emptyList(),
        val products: List<Product> = emptyList(),
        val popularProducts: List<Product> = emptyList(),
        val selectedCategoryId: String? = null,
        val query: String = ""
    )

    private val _state = MutableStateFlow(UiState(loading = true))
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        refresh()
    }

    fun refresh() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            try {
                _state.emit(_state.value.copy(loading = true, error = null))
                val categories = repository.getCategories()
                val allProducts = repository.getProducts()
                val selectedCategoryId = _state.value.selectedCategoryId
                val query = _state.value.query.trim()

                val filteredProducts = allProducts
                    .asSequence()
                    .filter { selectedCategoryId == null || it.categoryId == selectedCategoryId }
                    .filter {
                        query.isBlank() ||
                            it.title.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                    }
                    .toList()

                val popularProducts = allProducts
                    .asSequence()
                    .filter { it.isPopular }
                    .take(8)
                    .toList()

                _state.emit(
                    _state.value.copy(
                        loading = false,
                        categories = categories,
                        products = filteredProducts,
                        popularProducts = popularProducts
                    )
                )
            } catch (e: Exception) {
                _state.emit(_state.value.copy(loading = false, error = e.message ?: "Ошибка загрузки"))
            }
        }
    }

    fun onSelectCategory(categoryId: String?) {
        _state.value = _state.value.copy(selectedCategoryId = categoryId)
        refresh()
    }

    fun onSearch(query: String) {
        _state.value = _state.value.copy(query = query)
        refresh()
    }
}
