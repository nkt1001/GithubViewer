package com.apposum.domain.repository

import com.apposum.domain.entity.DataEntity
import com.apposum.domain.entity.GithubReposEntity

interface GithubRepoRepository {
    suspend fun findRepos(searchQuery: String = "", page: Int): DataEntity<GithubReposEntity>
}