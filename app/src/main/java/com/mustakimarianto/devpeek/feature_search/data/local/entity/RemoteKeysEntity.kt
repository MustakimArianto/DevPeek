package com.mustakimarianto.devpeek.feature_search.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Stores pagination metadata for each user in the cache.
 * This helps RemoteMediator know which page to fetch next.
 */
@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "query") val query: String,
    @ColumnInfo(name = "prev_key") val prevKey: Int?,
    @ColumnInfo(name = "next_key") val nextKey: Int?,
)