package com.example.furniturestore.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.furniturestore.data.local.preferences.TokenManager
import com.example.furniturestore.data.model.Order
import com.example.furniturestore.data.repository.FurnitureRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: FurnitureRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    data class UiState(
        val loading: Boolean = true,
        val error: String? = null,
        val name: String = "Покупатель",
        val email: String = "customer@furniture.store",
        val orders: List<Order> = emptyList(),
        val notificationsEnabled: Boolean = true,
        val discountsEnabled: Boolean = true
    ) {
        val ordersCount: Int get() = orders.size
    }

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _loggedOut = MutableStateFlow(false)
    val loggedOut: StateFlow<Boolean> = _loggedOut.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _state.emit(_state.value.copy(loading = true, error = null))
                val orders = repository.getOrders(userId = "guest")
                _state.emit(_state.value.copy(loading = false, orders = orders))
            } catch (e: Exception) {
                _state.emit(_state.value.copy(loading = false, error = e.message ?: "Ошибка загрузки профиля"))
            }
        }
    }

    fun toggleNotifications() {
        _state.value = _state.value.copy(notificationsEnabled = !_state.value.notificationsEnabled)
    }

    fun toggleDiscounts() {
        _state.value = _state.value.copy(discountsEnabled = !_state.value.discountsEnabled)
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearToken()
            _loggedOut.emit(true)
        }
    }
}
