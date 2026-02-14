package com.mustakimarianto.devpeek.feature_search.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mustakimarianto.devpeek.feature_search.data.local.entity.GithubUserEntity

@Dao
interface GithubUserDao {
    @Query(
        """
        SELECT * FROM github_users
        WHERE `query` = :query
        ORDER BY page ASC, id ASC
    """
    )
    fun getUsersByQueryPaging(query: String): PagingSource<Int, GithubUserEntity>

    @Query(
        """
        SELECT * FROM github_users
        WHERE `query` = :query
        ORDER BY page ASC, id ASC
    """
    )
    suspend fun getUsersByQuery(query: String): List<GithubUserEntity>

    @Query(
        """
        SELECT * FROM github_users
        WHERE `query` = :query AND page = :page
        ORDER BY id ASC
    """
    )
    suspend fun getUsersByQueryAndPage(query: String, page: Int): List<GithubUserEntity>

    @Query("SELECT MIN(cached_at) FROM github_users WHERE `query` = :query")
    suspend fun getOldestCacheTime(query: String): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<GithubUserEntity>)

    @Query("DELETE FROM github_users WHERE `query` = :query")
    suspend fun deleteByQuery(query: String)

    @Query("DELETE FROM github_users WHERE cached_at < :before")
    suspend fun evictOlderThan(before: Long)

    @Query("DELETE FROM github_users")
    suspend fun clearAll()
}