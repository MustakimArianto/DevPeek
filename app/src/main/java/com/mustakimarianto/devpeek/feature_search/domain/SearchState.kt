package com.mustakimarianto.devpeek.feature_search.domain

import com.mustakimarianto.devpeek.feature_search.domain.model.GithubUser

sealed interface SearchState {
    data object Idle : SearchState
    data object Loading : SearchState
    data object Empty : SearchState
    data class Success(val users: List<GithubUser>) : SearchState
    data class Error(val message: String) : SearchState
}