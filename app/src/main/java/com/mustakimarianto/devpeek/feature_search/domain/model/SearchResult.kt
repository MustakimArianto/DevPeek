package com.mustakimarianto.devpeek.feature_search.domain.model

data class SearchResult(
    val users: List<GithubUser>,
    val totalCount: Int,
)