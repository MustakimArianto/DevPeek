package com.mustakimarianto.devpeek.feature_menu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mustakimarianto.devpeek.feature_menu.presentation.MenuScreen
import com.mustakimarianto.devpeek.navigation.AppRoute

fun NavGraphBuilder.menuNavigation() {
    composable<AppRoute.Main> {
        MenuScreen()
    }
}