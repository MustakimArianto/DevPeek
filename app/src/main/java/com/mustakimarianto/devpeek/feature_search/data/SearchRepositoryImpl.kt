package com.mustakimarianto.devpeek.feature_search.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mustakimarianto.devpeek.core.data.local.AppDatabase
import com.mustakimarianto.devpeek.core.data.remote.GithubApi
import com.mustakimarianto.devpeek.feature_search.data.local.dao.GithubUserDao
import com.mustakimarianto.devpeek.feature_search.data.local.dao.RemoteKeysDao
import com.mustakimarianto.devpeek.feature_search.data.local.dao.SavedUserDao
import com.mustakimarianto.devpeek.feature_search.data.local.entity.SavedUserEntity
import com.mustakimarianto.devpeek.feature_search.data.paging.GithubUserRemoteMediator
import com.mustakimarianto.devpeek.feature_search.domain.SearchRepository
import com.mustakimarianto.devpeek.feature_search.domain.model.GithubUser
import com.mustakimarianto.devpeek.feature_search.domain.model.GithubUserDetail
import com.mustakimarianto.devpeek.feature_search.domain.model.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: GithubApi,
    private val database: AppDatabase,
    private val userDao: GithubUserDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val savedUserDao: SavedUserDao,
) : SearchRepository {

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 5
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun searchUsersPaging(query: String): Flow<PagingData<GithubUser>> {
        return Pager(
            PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE * 2,
            ),
            remoteMediator = GithubUserRemoteMediator(
                query = query,
                database = database,
                userDao = userDao,
                remoteKeysDao = remoteKeysDao,
                api = api,
            ),
            pagingSourceFactory = {
                // Room provides PagingSource automatically for queries returning PagingSource
                userDao.getUsersByQueryPaging(query)
            }
        ).flow.map { pagingData ->
            // Map from Entity to Domain model
            pagingData.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun getUserDetail(username: String): GithubUserDetail {
        val response = api.getUserDetail(username)
        return response.toDomain()
    }

    override suspend fun getUserRepositories(username: String, limit: Int): List<Repository> {
        val response = api.getUserRepositories(
            username = username,
            perPage = limit,
            sort = "updated"
        )
        return response
            .sortedByDescending { it.stargazersCount }
            .take(limit)
            .map { it.toDomain() }
    }

    override suspend fun isUserSaved(userId: Int): Boolean {
        return savedUserDao.isUserSaved(userId)
    }

    override suspend fun saveUser(user: GithubUserDetail) {
        val entity = SavedUserEntity.fromDomain(user)
        savedUserDao.insertUser(entity)
    }

    override suspend fun unsaveUser(userId: Int) {
        savedUserDao.deleteUserById(userId)
    }
}