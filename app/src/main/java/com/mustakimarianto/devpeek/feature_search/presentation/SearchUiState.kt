package com.mustakimarianto.devpeek.feature_search.presentation

import com.mustakimarianto.devpeek.feature_search.domain.model.UserType

data class SearchUiState(
    val query: String = "",
    val filter: UserType = UserType.ALL,
    val recentSearches: List<String> = listOf(),
)