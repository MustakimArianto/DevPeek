package com.mustakimarianto.devpeek.feature_saved.domain

import com.mustakimarianto.devpeek.core.domain.model.GithubUserDetail

sealed interface SavedState {
    data object Loading : SavedState
    data object Empty : SavedState
    data class Success(val users: Map<String, List<GithubUserDetail>>) : SavedState
    data class Error(val message: String) : SavedState
}