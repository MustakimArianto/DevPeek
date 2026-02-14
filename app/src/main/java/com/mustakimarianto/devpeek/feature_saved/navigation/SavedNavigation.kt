package com.mustakimarianto.devpeek.feature_saved.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.mustakimarianto.devpeek.core.ui_component.sharedViewModel
import com.mustakimarianto.devpeek.feature_saved.presentation.SavedDetailScreen
import com.mustakimarianto.devpeek.feature_saved.presentation.SavedListScreen
import com.mustakimarianto.devpeek.feature_saved.presentation.SavedViewModel
import com.mustakimarianto.devpeek.navigation.AppRoute

fun NavGraphBuilder.savedNavigation(navController: NavController) {
    navigation<AppRoute.Saved>(
        startDestination = SavedRoute.List
    ) {
        composable<SavedRoute.List> {
            val viewModel = it.sharedViewModel<SavedViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SavedListScreen(
                savedState = uiState.savedState,
                onUserClick = { user ->
                    navController.navigate(SavedRoute.Detail(username = user.login))
                },
                onDeleteUser = { userId ->
                    viewModel.deleteSavedUser(userId)
                },
                onClearAll = {
                    viewModel.deleteAllSavedUsers()
                }
            )
        }

        composable<SavedRoute.Detail> {
            val viewModel = it.sharedViewModel<SavedViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val route = it.toRoute<SavedRoute.Detail>()

            LaunchedEffect(route.username) {
                viewModel.loadUserDetail(route.username)
            }

            SavedDetailScreen(
                detailState = uiState.detailState,
                onBackClick = {
                    viewModel.clearDetailState()
                    navController.navigateUp()
                },
                onSaveToggle = viewModel::toggleSave,
                onRetry = { viewModel.loadUserDetail(route.username) },
            )
        }
    }
}