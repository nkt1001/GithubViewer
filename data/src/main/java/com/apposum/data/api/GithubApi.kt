package com.apposum.data.api

import com.apposum.data.entity.GithubReposData
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("search/repositories")
    suspend fun findRepos(@Query("q") query: String, @Query("page") page: Int = 1): GithubReposData
}