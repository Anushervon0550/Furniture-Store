package com.example.furniturestore.data.remote

import kotlinx.coroutines.delay

interface AuthService {
    suspend fun login(email: String, password: String): String
    suspend fun register(name: String, email: String, password: String): String
}

class FakeAuthService : AuthService {
    override suspend fun login(email: String, password: String): String {
        delay(400)
        require(email.contains("@") && password.length >= 6) { "Невалидные данные" }
        return "token-${email.hashCode()}"
    }

    override suspend fun register(name: String, email: String, password: String): String {
        delay(600)
        require(name.length >= 2 && email.contains("@") && password.length >= 6) { "Невалидные данные" }
        return "token-${email.hashCode()}"
    }
}
