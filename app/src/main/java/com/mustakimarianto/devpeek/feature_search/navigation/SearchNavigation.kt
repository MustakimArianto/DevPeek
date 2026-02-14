package com.mustakimarianto.devpeek.feature_search.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.mustakimarianto.devpeek.core.ui_component.UserDetailScreen
import com.mustakimarianto.devpeek.core.ui_component.sharedViewModel
import com.mustakimarianto.devpeek.feature_search.presentation.DiscoverScreen
import com.mustakimarianto.devpeek.feature_search.presentation.SearchViewModel
import com.mustakimarianto.devpeek.navigation.AppRoute

fun NavGraphBuilder.searchNavigation(navController: NavController) {
    navigation<AppRoute.Search>(
        startDestination = SearchRoute.Discover
    ) {
        composable<SearchRoute.Discover> {
            val viewModel = it.sharedViewModel<SearchViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val lazyPagingItems = viewModel.pagingDataFlow.collectAsLazyPagingItems()

            DiscoverScreen(
                query = uiState.query,
                filter = uiState.filter,
                recentSearches = uiState.recentSearches,
                lazyPagingItems = lazyPagingItems,
                onQueryChange = viewModel::onQueryChange,
                onFilterChange = viewModel::onFilterChange,
                onRecentSearchClick = viewModel::onRecentSearchClick,
                onClearRecentSearches = viewModel::onClearRecentSearches,
                onUserClick = { user ->
                    navController.navigate(SearchRoute.Detail(username = user.login))
                }
            )
        }

        composable<SearchRoute.Detail> {
            val viewModel = it.sharedViewModel<SearchViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val route = it.toRoute<SearchRoute.Detail>()

            LaunchedEffect(route.username) {
                viewModel.loadUserDetail(route.username)
            }

            UserDetailScreen(
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