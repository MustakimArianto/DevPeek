package com.mustakimarianto.devpeek.feature_saved.di

import com.mustakimarianto.devpeek.core.data.local.AppDatabase
import com.mustakimarianto.devpeek.core.data.local.dao.SavedUserDao
import com.mustakimarianto.devpeek.feature_saved.data.SavedRepositoryImpl
import com.mustakimarianto.devpeek.feature_saved.data.local.dao.SavedRepositoryDao
import com.mustakimarianto.devpeek.feature_saved.domain.SavedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SavedModule {
    @Provides
    @Singleton
    fun provideSavedRepository(
        savedUserDao: SavedUserDao,
        savedRepositoryDao: SavedRepositoryDao,
    ): SavedRepository {
        return SavedRepositoryImpl(savedUserDao, savedRepositoryDao)
    }

    @Provides
    @Singleton
    fun provideSavedRepositoryDao(
        database: AppDatabase
    ): SavedRepositoryDao {
        return database.savedRepositoryDao()
    }
}