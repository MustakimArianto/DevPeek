package com.mustakimarianto.devpeek.feature_search.di

import com.mustakimarianto.devpeek.core.data.local.AppDatabase
import com.mustakimarianto.devpeek.core.data.local.dao.SavedUserDao
import com.mustakimarianto.devpeek.core.data.remote.GithubApi
import com.mustakimarianto.devpeek.feature_search.data.SearchRepositoryImpl
import com.mustakimarianto.devpeek.feature_search.data.local.dao.GithubUserDao
import com.mustakimarianto.devpeek.feature_search.data.local.dao.RemoteKeysDao
import com.mustakimarianto.devpeek.feature_search.domain.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchModule {
    @Provides
    fun provideSearchRepository(
        api: GithubApi,
        appDatabase: AppDatabase,
        githubUserDao: GithubUserDao,
        remoteKeysDao: RemoteKeysDao,
        savedUserDao: SavedUserDao
    ): SearchRepository = SearchRepositoryImpl(
        api = api,
        database = appDatabase,
        userDao = githubUserDao,
        remoteKeysDao = remoteKeysDao,
        savedUserDao = savedUserDao
    )

    @Provides
    @Singleton
    fun provideGithubUserDao(
        database: AppDatabase
    ): GithubUserDao {
        return database.githubUserDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeysDao(
        database: AppDatabase
    ): RemoteKeysDao {
        return database.remoteKeysDao()
    }
}