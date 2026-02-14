package com.mustakimarianto.devpeek.feature_search.domain

import androidx.paging.PagingData
import com.mustakimarianto.devpeek.feature_search.domain.model.GithubUser
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    /**
     * Search users with Paging 3 support.
     * Returns a Flow of PagingData that handles:
     * - Incremental loading as user scrolls
     * - Caching for offline access
     * - Automatic refresh from network
     *
     * @param query Search query with optional filters (e.g., "kotlin type:user")
     * @return Flow of PagingData containing GithubUser items
     */
    fun searchUsersPaging(query: String): Flow<PagingData<GithubUser>>
}