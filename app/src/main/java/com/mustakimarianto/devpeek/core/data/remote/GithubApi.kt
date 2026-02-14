package com.mustakimarianto.devpeek.core.data.remote

import com.mustakimarianto.devpeek.feature_search.data.dto.GithubUserDetailDto
import com.mustakimarianto.devpeek.feature_search.data.dto.RepositoryDto
import com.mustakimarianto.devpeek.feature_search.data.dto.SearchUsersResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1,
    ): SearchUsersResponseDto

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String,
    ): GithubUserDetailDto

    @GET("users/{username}/repos")
    suspend fun getUserRepositories(
        @Path("username") username: String,
        @Query("per_page") perPage: Int = 100,
        @Query("sort") sort: String = "updated",
        @Query("direction") direction: String = "desc",
    ): List<RepositoryDto>
}