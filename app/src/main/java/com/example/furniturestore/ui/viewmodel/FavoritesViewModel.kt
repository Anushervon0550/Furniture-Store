package com.example.furniturestore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furniturestore.data.local.entity.FavoriteEntity
import com.example.furniturestore.data.repository.FurnitureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FurnitureRepository
) : ViewModel() {

    data class UiState(
        val items: List<FavoriteEntity> = emptyList(),
        val loading: Boolean = false,
        val error: String? = null
    )

    val state: StateFlow<UiState> = repository.observeFavorites()
        .map { list -> UiState(items = list) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState(loading = true))

    fun remove(entity: FavoriteEntity) {
        viewModelScope.launch { repository.removeFromFavorites(entity) }
    }
}
