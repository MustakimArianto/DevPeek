package com.mustakimarianto.devpeek.feature_search.navigation

import kotlinx.serialization.Serializable

interface SearchRoute {
    @Serializable
    data object Discover : SearchRoute

    @Serializable
    data object Detail : SearchRoute
}