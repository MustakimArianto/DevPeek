package com.mustakimarianto.devpeek.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mustakimarianto.devpeek.feature_menu.navigation.menuNavigation

@Composable
fun AppNavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = AppRoute.Main
    ) {
        menuNavigation()
    }
}