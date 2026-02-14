package com.mustakimarianto.devpeek.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mustakimarianto.devpeek.feature_search.data.local.dao.GithubUserDao
import com.mustakimarianto.devpeek.feature_search.data.local.dao.RemoteKeysDao
import com.mustakimarianto.devpeek.feature_search.data.local.entity.GithubUserEntity
import com.mustakimarianto.devpeek.feature_search.data.local.entity.RemoteKeysEntity


@Database(
    entities = [
        GithubUserEntity::class,
        RemoteKeysEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun githubUserDao(): GithubUserDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
