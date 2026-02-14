package com.mustakimarianto.devpeek.core.data.remote

import com.mustakimarianto.devpeek.feature_search.data.dto.SearchUsersResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1,
    ): SearchUsersResponseDto
}