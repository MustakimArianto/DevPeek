package com.mustakimarianto.devpeek.feature_menu.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mustakimarianto.devpeek.feature_menu.navigation.BottomNavItem
import com.mustakimarianto.devpeek.feature_search.navigation.searchNavigation
import com.mustakimarianto.devpeek.navigation.AppRoute

@Composable
fun MenuScreen() {
    val bottomNavController = rememberNavController()
    val currentBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            MainBottomBar(
                currentDestination = currentDestination, onNavigate = { route ->
                    bottomNavController.navigate(route) {
                        popUpTo(bottomNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = AppRoute.Search,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            searchNavigation(bottomNavController)

            composable<AppRoute.Saved> {
                // SavedScreen()
            }
            composable<AppRoute.Settings> {
                // SettingsScreen()
            }
        }
    }
}

@Composable
private fun MainBottomBar(
    currentDestination: NavDestination?, onNavigate: (AppRoute) -> Unit
) {
    val colors = NavigationBarItemDefaults.colors(
        // Active state
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
        selectedTextColor = MaterialTheme.colorScheme.onSurface,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        // Inactive state
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        // Disabled state
        disabledIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
        disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        BottomNavItem.entries.forEach { item ->
            val isSelected =
                currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                colors = colors,
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.titleRes),
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.titleRes),
                        style = MaterialTheme.typography.labelSmall,
                    )
                })
        }
    }
}