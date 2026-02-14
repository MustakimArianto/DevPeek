package com.mustakimarianto.devpeek.feature_saved.data

import com.mustakimarianto.devpeek.core.data.local.dao.SavedUserDao
import com.mustakimarianto.devpeek.core.data.local.entity.SavedUserEntity
import com.mustakimarianto.devpeek.core.domain.model.GithubUserDetail
import com.mustakimarianto.devpeek.core.domain.model.RepositoryModel
import com.mustakimarianto.devpeek.feature_saved.data.local.dao.SavedRepositoryDao
import com.mustakimarianto.devpeek.feature_saved.data.local.entity.SavedRepositoryEntity
import com.mustakimarianto.devpeek.feature_saved.domain.SavedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SavedRepositoryImpl @Inject constructor(
    private val savedUserDao: SavedUserDao,
    private val savedRepositoryDao: SavedRepositoryDao,
) : SavedRepository {

    override fun getSavedUsers(): Flow<List<GithubUserDetail>> {
        return savedUserDao.getAllSavedUsers().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getSavedUserById(userId: Int): GithubUserDetail? {
        return savedUserDao.getSavedUserById(userId)?.toDomain()
    }

    override suspend fun getSavedUserByUsername(username: String): GithubUserDetail? {
        return savedUserDao.getSavedUserByUsername(username)?.toDomain()
    }

    override suspend fun getSavedRepositories(userId: Int): List<RepositoryModel> {
        return savedRepositoryDao.getRepositoriesByUserId(userId).map { it.toDomain() }
    }

    override suspend fun saveUserWithRepositories(
        user: GithubUserDetail, repositories: List<RepositoryModel>
    ) {
        // Save user
        val userEntity = SavedUserEntity.fromDomain(user)
        savedUserDao.insertUser(userEntity)

        // Delete old repositories for this user
        savedRepositoryDao.deleteRepositoriesByUserId(user.id)

        // Save new repositories
        if (repositories.isNotEmpty()) {
            val repoEntities = repositories.map { repo ->
                SavedRepositoryEntity.fromDomain(repo, user.id)
            }
            savedRepositoryDao.insertRepositories(repoEntities)
        }
    }

    override suspend fun deleteSavedUser(userId: Int) {
        // Delete user's repositories first
        savedRepositoryDao.deleteRepositoriesByUserId(userId)
        // Then delete user
        savedUserDao.deleteUserById(userId)
    }

    override suspend fun deleteAllSavedUsers() {
        // Delete all repositories first
        savedRepositoryDao.deleteAllRepositories()
        // Then delete all users
        savedUserDao.deleteAllUsers()
    }
}