package com.mustakimarianto.devpeek.feature_search.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mustakimarianto.devpeek.feature_search.domain.model.GithubUser

@Entity(tableName = "github_users")
data class GithubUserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "html_url") val htmlUrl: String,

    @ColumnInfo(name = "query") val query: String,
    @ColumnInfo(name = "page") val page: Int,
    @ColumnInfo(name = "cached_at") val cachedAt: Long = System.currentTimeMillis(),
) {
    fun toDomain(): GithubUser {
        return GithubUser(
            id = id,
            login = login,
            avatarUrl = avatarUrl,
            type = type,
            htmlUrl = htmlUrl
        )
    }

    companion object {
        fun List<GithubUserEntity>.toDomain(): List<GithubUser> {
            return this.map { it.toDomain() }
        }
    }
}