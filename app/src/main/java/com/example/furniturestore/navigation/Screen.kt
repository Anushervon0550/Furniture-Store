package com.example.furniturestore.navigation

sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "Главная")
    data object Cart : Screen("cart", "Корзина")
    data object Favorites : Screen("favorites", "Избранное")
    data object Profile : Screen("profile", "Профиль")
    data object ProductDetails : Screen("product_details/{productId}", "Товар") {
        fun create(productId: String) = "product_details/$productId"
    }
    data object Splash : Screen("splash", "Заставка")
    data object Login : Screen("login", "Вход")
    data object Register : Screen("register", "Регистрация")
}
