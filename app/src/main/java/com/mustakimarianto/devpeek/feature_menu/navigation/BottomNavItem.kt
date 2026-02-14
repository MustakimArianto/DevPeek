package com.mustakimarianto.devpeek.feature_menu.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mustakimarianto.devpeek.R
import com.mustakimarianto.devpeek.navigation.AppRoute

enum class BottomNavItem(
    @param:StringRes val titleRes: Int,
    @param:DrawableRes val icon: Int,
    val route: AppRoute,
) {
    Search(
        titleRes = R.string.nav_search,
        icon = R.drawable.baseline_search_24,
        route = AppRoute.Search
    ),
    Saved(
        titleRes = R.string.nav_saved,
        icon = R.drawable.baseline_bookmark_add_24,
        route = AppRoute.Saved
    ),
    Settings(
        titleRes = R.string.nav_settings,
        icon = R.drawable.baseline_settings_24,
        route = AppRoute.Settings
    )
}