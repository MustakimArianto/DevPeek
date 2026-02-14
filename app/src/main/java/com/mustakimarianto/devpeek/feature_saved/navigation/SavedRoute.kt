package com.mustakimarianto.devpeek.feature_saved.navigation

import kotlinx.serialization.Serializable

interface SavedRoute {
    @Serializable
    data object List : SavedRoute

    @Serializable
    data class Detail(val username: String) : SavedRoute
}