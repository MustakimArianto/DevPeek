package com.mustakimarianto.devpeek.feature_search.domain.model

import androidx.annotation.Keep

@Keep
data class Repository(
    val id: Int,
    val name: String,
    val fullName: String,
    val description: String?,
    val language: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val htmlUrl: String,
    val updatedAt: String,
)