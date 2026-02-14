package com.mustakimarianto.devpeek.feature_saved.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mustakimarianto.devpeek.feature_saved.data.local.entity.SavedRepositoryEntity

@Dao
interface SavedRepositoryDao {
    /**
     * Get all repositories for a specific user
     */
    @Query("SELECT * FROM saved_repositories WHERE userId = :userId ORDER BY stargazersCount DESC")
    suspend fun getRepositoriesByUserId(userId: Int): List<SavedRepositoryEntity>

    /**
     * Insert repositories (replaces if exists)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepositories(repositories: List<SavedRepositoryEntity>)

    /**
     * Delete all repositories for a specific user
     */
    @Query("DELETE FROM saved_repositories WHERE userId = :userId")
    suspend fun deleteRepositoriesByUserId(userId: Int)

    /**
     * Delete all saved repositories
     */
    @Query("DELETE FROM saved_repositories")
    suspend fun deleteAllRepositories()
}