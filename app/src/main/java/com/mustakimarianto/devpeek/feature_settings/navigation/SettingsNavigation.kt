package com.mustakimarianto.devpeek.feature_settings.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mustakimarianto.devpeek.core.ui_component.sharedViewModel
import com.mustakimarianto.devpeek.feature_settings.presentation.SettingsScreen
import com.mustakimarianto.devpeek.feature_settings.presentation.SettingsViewModel
import com.mustakimarianto.devpeek.navigation.AppRoute

fun NavGraphBuilder.settingsNavigation(navController: NavController) {
    navigation<AppRoute.Settings>(
        startDestination = SettingsRoute.Root
    ) {
        composable<SettingsRoute.Root> {
            val viewModel = it.sharedViewModel<SettingsViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SettingsScreen(
                isDarkMode = uiState.isDarkMode,
                onDarkModeToggle = viewModel::toggleDarkMode
            )
        }
    }
}