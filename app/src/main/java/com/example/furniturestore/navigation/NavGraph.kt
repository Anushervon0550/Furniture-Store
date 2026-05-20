package com.example.furniturestore.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.furniturestore.ui.screens.HomeScreen
import com.example.furniturestore.ui.screens.CartScreen
import com.example.furniturestore.ui.screens.FavoritesScreen
import com.example.furniturestore.ui.screens.ProductDetailsScreen
import com.example.furniturestore.ui.screens.ProfileScreen
import com.example.furniturestore.ui.screens.LoginScreen
import com.example.furniturestore.ui.screens.RegisterScreen
import com.example.furniturestore.ui.screens.SplashScreen
import com.example.furniturestore.ui.viewmodel.HomeViewModel
import com.example.furniturestore.ui.viewmodel.CartViewModel
import com.example.furniturestore.ui.viewmodel.FavoritesViewModel
import com.example.furniturestore.ui.viewmodel.ProductDetailsViewModel
import com.example.furniturestore.ui.viewmodel.ProfileViewModel
import com.example.furniturestore.ui.viewmodel.AuthViewModel
import com.example.furniturestore.ui.viewmodel.SplashViewModel

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            val vm: SplashViewModel = hiltViewModel()
            val token = vm.token.collectAsState()
            SplashScreen()
            LaunchedEffect(token.value) {
                val next = if (token.value.isNullOrBlank()) Screen.Login.route else Screen.Home.route
                navController.navigate(next) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }
        composable(Screen.Login.route) {
            val vm: AuthViewModel = hiltViewModel()
            val state = vm.state.collectAsState()
            LoginScreen(
                loading = state.value.loading,
                error = state.value.error,
                onLogin = { email, pass -> vm.login(email, pass) },
                onGoRegister = { navController.navigate(Screen.Register.route) }
            )
            LaunchedEffect(state.value.success) {
                if (state.value.success) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
        }
        composable(Screen.Register.route) {
            val vm: AuthViewModel = hiltViewModel()
            val state = vm.state.collectAsState()
            RegisterScreen(
                loading = state.value.loading,
                error = state.value.error,
                onRegister = { name, email, pass -> vm.register(name, email, pass) },
                onGoLogin = { navController.navigate(Screen.Login.route) }
            )
            LaunchedEffect(state.value.success) {
                if (state.value.success) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            }
        }
        composable(Screen.Home.route) {
            val vm: HomeViewModel = hiltViewModel()
            val cartVm: CartViewModel = hiltViewModel()
            val state = vm.state.collectAsState()
            when {
                state.value.loading -> CenterBox { CircularProgressIndicator() }
                state.value.error != null -> CenterBox { Text(text = state.value.error!!, color = MaterialTheme.colorScheme.error) }
                else -> HomeScreen(
                    products = state.value.products,
                    popularProducts = state.value.popularProducts,
                    categories = state.value.categories,
                    selectedCategoryId = state.value.selectedCategoryId,
                    onSelectCategory = vm::onSelectCategory,
                    onSearch = vm::onSearch,
                    onOpenProduct = { p -> navController.navigate(Screen.ProductDetails.create(p.id)) },
                    onAddToCart = { p -> cartVm.add(p) }
                )
            }
        }
        composable(Screen.Cart.route) {
            val vm: CartViewModel = hiltViewModel()
            val state = vm.state.collectAsState()
            CartScreen(
                items = state.value.items,
                total = state.value.total,
                onIncrease = vm::increase,
                onDecrease = vm::decrease,
                onRemove = vm::remove,
                onCheckout = vm::checkout,
                placing = vm.placing.collectAsState().value,
                orderId = vm.orderId.collectAsState().value,
                orderError = vm.orderError.collectAsState().value
            )
        }
        composable(Screen.Favorites.route) {
            val vm: FavoritesViewModel = hiltViewModel()
            val state = vm.state.collectAsState()
            FavoritesScreen(
                items = state.value.items,
                onRemove = vm::remove,
                onOpen = { fav -> navController.navigate(Screen.ProductDetails.create(fav.productId)) }
            )
        }
        composable(Screen.Profile.route) {
            val vm: ProfileViewModel = hiltViewModel()
            val state = vm.state.collectAsState()
            val loggedOut = vm.loggedOut.collectAsState()

            LaunchedEffect(Unit) {
                vm.refresh()
            }

            LaunchedEffect(loggedOut.value) {
                if (loggedOut.value) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            ProfileScreen(
                name = state.value.name,
                email = state.value.email,
                orders = state.value.orders,
                loading = state.value.loading,
                error = state.value.error,
                notificationsEnabled = state.value.notificationsEnabled,
                discountsEnabled = state.value.discountsEnabled,
                onToggleNotifications = vm::toggleNotifications,
                onToggleDiscounts = vm::toggleDiscounts,
                onRefresh = vm::refresh,
                onLogout = vm::logout
            )
        }
        composable(
            route = Screen.ProductDetails.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {
            val detailsVm: ProductDetailsViewModel = hiltViewModel()
            val cartVm: CartViewModel = hiltViewModel()
            val state = detailsVm.state.collectAsState()
            ProductDetailsScreen(
                product = state.value.product,
                quantity = state.value.quantity,
                onIncrease = detailsVm::increase,
                onDecrease = detailsVm::decrease,
                isFavorite = state.value.isFavorite,
                onToggleFavorite = detailsVm::toggleFavorite,
                onAddToCart = {
                    state.value.product?.let { p ->
                        cartVm.add(p, state.value.quantity)
                    }
                }
            )
        }
    }
}

@Composable
private fun CenterBox(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        content()
    }
}
