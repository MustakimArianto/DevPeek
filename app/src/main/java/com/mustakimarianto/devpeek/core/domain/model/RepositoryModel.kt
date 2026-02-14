package com.mustakimarianto.devpeek.core.domain.model

import androidx.annotation.Keep

@Keep
data class RepositoryModel(
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