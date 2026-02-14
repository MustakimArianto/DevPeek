package com.mustakimarianto.devpeek.navigation

import kotlinx.serialization.Serializable

interface AppRoute {
    @Serializable
    data object Main : AppRoute

    @Serializable
    data object Search : AppRoute

    @Serializable
    data object Saved : AppRoute

    @Serializable
    data object Settings : AppRoute
}