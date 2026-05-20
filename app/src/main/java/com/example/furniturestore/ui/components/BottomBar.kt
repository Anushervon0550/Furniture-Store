package com.example.furniturestore.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.furniturestore.navigation.Screen

private data class BottomItem(
    val screen: Screen,
    val icon: ImageVector
)

@Composable
fun BottomBar(navController: NavHostController) {
    val hideOnRoutes = setOf(
        Screen.Splash.route,
        Screen.Login.route,
        Screen.Register.route
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldHide = currentRoute in hideOnRoutes || currentRoute?.startsWith("product_details/") == true
    if (shouldHide) {
        return
    }
    val items = listOf(
        BottomItem(Screen.Home, Icons.Outlined.Home),
        BottomItem(Screen.Favorites, Icons.Outlined.FavoriteBorder),
        BottomItem(Screen.Cart, Icons.Outlined.ShoppingCart),
        BottomItem(Screen.Profile, Icons.Outlined.Person)
    )
    NavigationBar {
        val currentDestination = navBackStackEntry?.destination
        items.forEach { item ->
            NavigationBarItem(
                selected = isSelected(currentDestination, item.screen.route),
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.screen.title) },
                label = { Text(item.screen.title) }
            )
        }
    }
}

private fun isSelected(current: NavDestination?, route: String): Boolean {
    return current?.hierarchy?.any { it.route == route } == true
}
