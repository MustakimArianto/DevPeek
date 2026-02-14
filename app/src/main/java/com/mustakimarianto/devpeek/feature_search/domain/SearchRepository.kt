package com.mustakimarianto.devpeek.feature_search.domain

import androidx.paging.PagingData
import com.mustakimarianto.devpeek.core.domain.model.GithubUserDetail
import com.mustakimarianto.devpeek.core.domain.model.RepositoryModel
import com.mustakimarianto.devpeek.feature_search.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    /**
     * Search users with Paging 3 support.
     * Returns a Flow of PagingData that handles:
     * - Incremental loading as user scrolls
     * - Caching for offline access
     * - Automatic refresh from network
     *
     * @param query Search query with optional filters (e.g., "kotlin type:user")
     * @return Flow of PagingData containing GithubUser items
     */
    fun searchUsersPaging(query: String): Flow<PagingData<GithubUser>>

    /**
     * Get detailed information about a specific user
     *
     * @param username GitHub username
     * @return GithubUserDetail containing full user information
     * @throws Exception if user not found or network error
     */
    suspend fun getUserDetail(username: String): GithubUserDetail

    /**
     * Get a user's public repositories
     *
     * @param username GitHub username
     * @param limit Maximum number of repositories to return
     * @return List of Repository objects sorted by stars
     * @throws Exception if error occurs
     */
    suspend fun getUserRepositories(username: String, limit: Int = 100): List<RepositoryModel>

    /**
     * Check if a user is saved locally
     *
     * @param userId GitHub user ID
     * @return true if user is saved, false otherwise
     */
    suspend fun isUserSaved(userId: Int): Boolean

    /**
     * Save a user to local database
     *
     * @param user GithubUserDetail to save
     * @throws Exception if save fails
     */
    suspend fun saveUser(user: GithubUserDetail)

    /**
     * Remove a user from saved list
     *
     * @param userId GitHub user ID
     * @throws Exception if deletion fails
     */
    suspend fun unsaveUser(userId: Int)
}