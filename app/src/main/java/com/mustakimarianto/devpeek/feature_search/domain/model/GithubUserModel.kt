package com.mustakimarianto.devpeek.feature_search.domain.model

import androidx.annotation.Keep

@Keep
data class GithubUser(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val type: String,
    val htmlUrl: String,
)