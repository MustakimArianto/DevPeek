package com.mustakimarianto.devpeek.feature_search.data.dto

import com.mustakimarianto.devpeek.core.domain.model.GithubUserDetail
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubUserDetailDto(
    @param:Json(name = "id")
    val id: Int,

    @param:Json(name = "login")
    val login: String,

    @param:Json(name = "name")
    val name: String?,

    @param:Json(name = "avatar_url")
    val avatarUrl: String,

    @param:Json(name = "bio")
    val bio: String?,

    @param:Json(name = "company")
    val company: String?,

    @param:Json(name = "location")
    val location: String?,

    @param:Json(name = "email")
    val email: String?,

    @param:Json(name = "blog")
    val blog: String?,

    @param:Json(name = "twitter_username")
    val twitterUsername: String?,

    @param:Json(name = "public_repos")
    val publicRepos: Int,

    @param:Json(name = "public_gists")
    val publicGists: Int,

    @param:Json(name = "followers")
    val followers: Int,

    @param:Json(name = "following")
    val following: Int,

    @param:Json(name = "created_at")
    val createdAt: String,

    @param:Json(name = "updated_at")
    val updatedAt: String,

    @param:Json(name = "html_url")
    val htmlUrl: String,

    @param:Json(name = "type")
    val type: String,
) {
    fun toDomain(): GithubUserDetail {
        return GithubUserDetail(
            id = id,
            login = login,
            name = name,
            avatarUrl = avatarUrl,
            bio = bio,
            company = company,
            location = location,
            email = email,
            blog = blog,
            twitterUsername = twitterUsername,
            publicRepos = publicRepos,
            publicGists = publicGists,
            followers = followers,
            following = following,
            createdAt = createdAt,
            updatedAt = updatedAt,
            htmlUrl = htmlUrl,
            type = type,
        )
    }
}