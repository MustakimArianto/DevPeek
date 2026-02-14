package com.mustakimarianto.devpeek.feature_search.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubUserDto(
    @param:Json(name = "id") val id: Int,
    @param:Json(name = "login") val login: String,
    @param:Json(name = "avatar_url") val avatarUrl: String,
    @param:Json(name = "type") val type: String,
    @param:Json(name = "html_url") val htmlUrl: String,
)

