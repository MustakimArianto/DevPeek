package com.mustakimarianto.devpeek.core.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class GitHubHeadersInterceptor(private val token: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/vnd.github+json")
            .addHeader("X-GitHub-Api-Version", "2022-11-28")
        if (!token.isNullOrBlank()) request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}