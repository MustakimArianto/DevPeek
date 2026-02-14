package com.mustakimarianto.devpeek.feature_saved.domain

import com.mustakimarianto.devpeek.core.domain.model.GithubUserDetail
import com.mustakimarianto.devpeek.core.domain.model.RepositoryModel
import kotlinx.coroutines.flow.Flow

interface SavedRepository {
    /**
     * Get all saved users as a Flow for reactive updates
     *
     * @return Flow of list of saved GithubUserDetail
     */
    fun getSavedUsers(): Flow<List<GithubUserDetail>>

    /**
     * Get a specific saved user by ID
     *
     * @param userId GitHub user ID
     * @return GithubUserDetail if found, null otherwise
     */
    suspend fun getSavedUserById(userId: Int): GithubUserDetail?

    /**
     * Get a specific saved user by username (for offline-first detail loading)
     *
     * @param username GitHub username
     * @return GithubUserDetail if found, null otherwise
     */
    suspend fun getSavedUserByUsername(username: String): GithubUserDetail?

    /**
     * Get saved repositories for a specific user
     *
     * @param userId GitHub user ID
     * @return List of saved repositories
     */
    suspend fun getSavedRepositories(userId: Int): List<RepositoryModel>

    /**
     * Save a user with their top repositories
     *
     * @param user GithubUserDetail to save
     * @param repositories List of top repositories to save with the user
     * @throws Exception if save fails
     */
    suspend fun saveUserWithRepositories(user: GithubUserDetail, repositories: List<RepositoryModel>)

    /**
     * Delete a saved user and their repositories
     *
     * @param userId GitHub user ID
     * @throws Exception if deletion fails
     */
    suspend fun deleteSavedUser(userId: Int)

    /**
     * Delete all saved users and their repositories
     *
     * @throws Exception if deletion fails
     */
    suspend fun deleteAllSavedUsers()
}