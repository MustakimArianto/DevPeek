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
import com.mustakimarianto.devpeek.feature_search.data.paging.GithubUserRemoteMediator
import com.mustakimarianto.devpeek.feature_search.domain.SearchRepository
import com.mustakimarianto.devpeek.feature_search.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: GithubApi,
    private val database: AppDatabase,
    private val userDao: GithubUserDao,
    private val remoteKeysDao: RemoteKeysDao,
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
}