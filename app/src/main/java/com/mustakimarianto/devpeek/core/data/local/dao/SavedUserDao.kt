package com.mustakimarianto.devpeek.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mustakimarianto.devpeek.core.data.local.entity.SavedUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedUserDao {
    @Query("SELECT * FROM saved_users ORDER BY savedAt DESC")
    fun getAllSavedUsers(): Flow<List<SavedUserEntity>>

    @Query("SELECT * FROM saved_users WHERE id = :userId")
    suspend fun getSavedUserById(userId: Int): SavedUserEntity?

    @Query("SELECT * FROM saved_users WHERE login = :username LIMIT 1")
    suspend fun getSavedUserByUsername(username: String): SavedUserEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM saved_users WHERE id = :userId)")
    suspend fun isUserSaved(userId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: SavedUserEntity)

    @Query("DELETE FROM saved_users WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)

    @Query("DELETE FROM saved_users")
    suspend fun deleteAllUsers()
}