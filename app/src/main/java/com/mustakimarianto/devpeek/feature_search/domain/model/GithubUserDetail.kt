package com.mustakimarianto.devpeek.feature_search.domain.model

import androidx.annotation.Keep

@Keep
data class GithubUserDetail(
    val id: Int,
    val login: String,
    val name: String?,
    val avatarUrl: String,
    val bio: String?,
    val company: String?,
    val location: String?,
    val email: String?,
    val blog: String?,
    val twitterUsername: String?,
    val publicRepos: Int,
    val publicGists: Int,
    val followers: Int,
    val following: Int,
    val createdAt: String,
    val updatedAt: String,
    val htmlUrl: String,
    val type: String,
)