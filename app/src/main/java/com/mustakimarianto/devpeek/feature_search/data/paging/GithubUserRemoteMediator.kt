package com.mustakimarianto.devpeek.feature_search.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mustakimarianto.devpeek.core.data.local.AppDatabase
import com.mustakimarianto.devpeek.core.data.remote.GithubApi
import com.mustakimarianto.devpeek.feature_search.data.dto.SearchUsersResponseDto.Companion.toEntity
import com.mustakimarianto.devpeek.feature_search.data.local.dao.GithubUserDao
import com.mustakimarianto.devpeek.feature_search.data.local.dao.RemoteKeysDao
import com.mustakimarianto.devpeek.feature_search.data.local.entity.GithubUserEntity
import com.mustakimarianto.devpeek.feature_search.data.local.entity.RemoteKeysEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * RemoteMediator handles fetching paginated data from network and caching in Room.
 */
@OptIn(ExperimentalPagingApi::class)
class GithubUserRemoteMediator @Inject constructor(
    private val query: String,
    private val database: AppDatabase,
    private val userDao: GithubUserDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val api: GithubApi,
) : RemoteMediator<Int, GithubUserEntity>() {

    override suspend fun initialize(): InitializeAction {
        // Check if cache is stale (older than 1 hour)
        val cacheTimeout = System.currentTimeMillis() - (60 * 60 * 1000) // 1 hour
        val oldestCache = userDao.getOldestCacheTime(query)

        return if (oldestCache != null && oldestCache > cacheTimeout) {
            // Cache is fresh, skip initial refresh
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Cache is stale or doesn't exist, trigger refresh
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GithubUserEntity>
    ): MediatorResult {
        return try {
            // 1. Determine which page to load
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    // Initial load or pull-to-refresh
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    // We don't support prepending (loading older pages)
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    // Loading next page
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )

                    nextKey
                }
            }

            // 2. Fetch from API
            val response = api.searchUsers(
                query = query,
                page = page,
                perPage = state.config.pageSize
            )

            val users = response.items
            val endOfPaginationReached = users.isEmpty() ||
                    (state.config.pageSize * page) >= response.totalCount

            // 3. Save to database in a transaction
            database.withTransaction {
                // Clear cache if this is a refresh
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.deleteByQuery(query)
                    userDao.deleteByQuery(query)
                }

                // Calculate remote keys
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = users.map { user ->
                    RemoteKeysEntity(
                        userId = user.id,
                        query = query,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                // Insert remote keys and users
                remoteKeysDao.insertAll(keys)
                userDao.insertAll(users.toEntity(query = query, page = page))
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, GithubUserEntity>
    ): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { user ->
            remoteKeysDao.getRemoteKeyByUserId(user.id, query)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, GithubUserEntity>
    ): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { user ->
                remoteKeysDao.getRemoteKeyByUserId(user.id, query)
            }
        }
    }
}