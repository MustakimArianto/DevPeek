package com.mustakimarianto.devpeek.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mustakimarianto.devpeek.core.domain.model.GithubUserDetail

@Entity(tableName = "saved_users")
data class SavedUserEntity(
    @PrimaryKey val id: Int,
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
    val savedAt: Long = System.currentTimeMillis(),
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

    companion object {
        fun fromDomain(user: GithubUserDetail): SavedUserEntity {
            return SavedUserEntity(
                id = user.id,
                login = user.login,
                name = user.name,
                avatarUrl = user.avatarUrl,
                bio = user.bio,
                company = user.company,
                location = user.location,
                email = user.email,
                blog = user.blog,
                twitterUsername = user.twitterUsername,
                publicRepos = user.publicRepos,
                publicGists = user.publicGists,
                followers = user.followers,
                following = user.following,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt,
                htmlUrl = user.htmlUrl,
                type = user.type,
            )
        }
    }
}