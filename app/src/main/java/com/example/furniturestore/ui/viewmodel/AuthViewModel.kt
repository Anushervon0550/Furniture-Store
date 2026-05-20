package com.example.furniturestore.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furniturestore.data.local.preferences.TokenManager
import com.example.furniturestore.data.remote.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val error: String? = null,
        val success: Boolean = false
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        val normalizedEmail = email.trim()
        when {
            !Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches() -> {
                _state.value = UiState(error = "Введите корректный email")
                return
            }

            password.length < 6 -> {
                _state.value = UiState(error = "Пароль должен быть не менее 6 символов")
                return
            }
        }

        viewModelScope.launch {
            try {
                _state.value = UiState(loading = true)
                val token = authService.login(normalizedEmail, password)
                tokenManager.saveToken(token)
                _state.value = UiState(success = true)
            } catch (e: Exception) {
                _state.value = UiState(error = e.message ?: "Ошибка входа")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        val normalizedName = name.trim()
        val normalizedEmail = email.trim()
        when {
            normalizedName.length < 2 -> {
                _state.value = UiState(error = "Имя должно быть не менее 2 символов")
                return
            }

            !Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches() -> {
                _state.value = UiState(error = "Введите корректный email")
                return
            }

            password.length < 6 -> {
                _state.value = UiState(error = "Пароль должен быть не менее 6 символов")
                return
            }
        }

        viewModelScope.launch {
            try {
                _state.value = UiState(loading = true)
                val token = authService.register(normalizedName, normalizedEmail, password)
                tokenManager.saveToken(token)
                _state.value = UiState(success = true)
            } catch (e: Exception) {
                _state.value = UiState(error = e.message ?: "Ошибка регистрации")
            }
        }
    }
}
