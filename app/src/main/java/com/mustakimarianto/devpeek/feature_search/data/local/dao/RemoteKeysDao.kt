package com.mustakimarianto.devpeek.feature_search.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mustakimarianto.devpeek.feature_search.data.local.entity.RemoteKeysEntity

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeysEntity>)

    @Query("SELECT * FROM remote_keys WHERE user_id = :userId AND `query` = :query")
    suspend fun getRemoteKeyByUserId(userId: Int, query: String): RemoteKeysEntity?

    @Query("DELETE FROM remote_keys WHERE `query` = :query")
    suspend fun deleteByQuery(query: String)

    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()
}