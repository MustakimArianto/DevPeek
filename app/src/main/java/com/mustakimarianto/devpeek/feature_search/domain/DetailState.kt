package com.mustakimarianto.devpeek.feature_search.domain

import com.mustakimarianto.devpeek.core.domain.model.GithubUserDetail
import com.mustakimarianto.devpeek.core.domain.model.RepositoryModel

sealed interface DetailState {
    data object Idle : DetailState
    data object Loading : DetailState

    data class Success(
        val user: GithubUserDetail,
        val topRepos: List<RepositoryModel>,
        val isSaved: Boolean,
    ) : DetailState

    data class Error(val message: String) : DetailState
}