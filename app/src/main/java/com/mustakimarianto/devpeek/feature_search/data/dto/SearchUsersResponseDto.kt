package com.mustakimarianto.devpeek.feature_search.data.dto

import com.mustakimarianto.devpeek.feature_search.data.local.entity.GithubUserEntity
import com.mustakimarianto.devpeek.feature_search.domain.model.GithubUser
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchUsersResponseDto(
    @param:Json(name = "total_count") val totalCount: Int,
    @param:Json(name = "items") val items: List<GithubUserDto>,
) {
    companion object {
        fun List<GithubUserDto>.toEntity(query: String, page: Int): List<GithubUserEntity> {
            return this.map {
                GithubUserEntity(
                    id = it.id,
                    login = it.login,
                    avatarUrl = it.avatarUrl,
                    type = it.type,
                    htmlUrl = it.htmlUrl,
                    query = query,
                    page = page,
                )
            }
        }

        fun List<GithubUserDto>.toDomain(): List<GithubUser> {
            return this.map {
                GithubUser(
                    id = it.id,
                    login = it.login,
                    avatarUrl = it.avatarUrl,
                    type = it.type,
                    htmlUrl = it.htmlUrl,
                )
            }
        }
    }
}