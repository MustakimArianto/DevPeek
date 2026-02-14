package com.mustakimarianto.devpeek.feature_search.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mustakimarianto.devpeek.feature_search.data.local.entity.SavedUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: SavedUserEntity)

    @Query("SELECT * FROM saved_users ORDER BY savedAt DESC")
    fun getAllSavedUsers(): Flow<List<SavedUserEntity>>

    @Query("SELECT * FROM saved_users WHERE id = :userId")
    suspend fun getUserById(userId: Int): SavedUserEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM saved_users WHERE id = :userId)")
    suspend fun isUserSaved(userId: Int): Boolean

    @Query("DELETE FROM saved_users WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)

    @Query("DELETE FROM saved_users")
    suspend fun deleteAllUsers()

    @Query("SELECT COUNT(*) FROM saved_users")
    suspend fun getSavedUserCount(): Int
}