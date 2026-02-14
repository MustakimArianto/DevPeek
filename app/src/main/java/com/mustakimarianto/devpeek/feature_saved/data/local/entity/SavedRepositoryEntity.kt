package com.mustakimarianto.devpeek.feature_saved.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mustakimarianto.devpeek.core.domain.model.RepositoryModel

@Entity(tableName = "saved_repositories")
data class SavedRepositoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // Foreign key to saved_users
    val repoId: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val language: String?,
    val stargazersCount: Int,
    val forksCount: Int,
    val watchersCount: Int,
    val htmlUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val savedAt: Long = System.currentTimeMillis(),
) {
    fun toDomain(): RepositoryModel {
        return RepositoryModel(
            id = repoId.toInt(),
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

    companion object {
        fun fromDomain(repo: RepositoryModel, userId: Int): SavedRepositoryEntity {
            return SavedRepositoryEntity(
                userId = userId,
                repoId = repo.id.toLong(),
                name = repo.name,
                fullName = repo.fullName,
                description = repo.description,
                language = repo.language,
                stargazersCount = repo.stargazersCount,
                forksCount = repo.forksCount,
                watchersCount = 0,
                htmlUrl = repo.htmlUrl,
                createdAt = "",
                updatedAt = repo.updatedAt,
            )
        }
    }
}