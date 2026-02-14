package com.mustakimarianto.devpeek.feature_search.data.dto

import com.mustakimarianto.devpeek.feature_search.domain.model.Repository
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepositoryDto(
    @param:Json(name = "id")
    val id: Int,

    @param:Json(name = "name")
    val name: String,

    @param:Json(name = "full_name")
    val fullName: String,

    @param:Json(name = "description")
    val description: String?,

    @param:Json(name = "language")
    val language: String?,

    @param:Json(name = "stargazers_count")
    val stargazersCount: Int,

    @param:Json(name = "forks_count")
    val forksCount: Int,

    @param:Json(name = "html_url")
    val htmlUrl: String,

    @param:Json(name = "updated_at")
    val updatedAt: String,
) {
    fun toDomain(): Repository {
        return Repository(
            id = id,
            name = name,
            fullName = fullName,
            description = description,
            language = language,
            stargazersCount = stargazersCount,
            forksCount = forksCount,
            htmlUrl = htmlUrl,
            updatedAt = updatedAt,
        )
    }
}