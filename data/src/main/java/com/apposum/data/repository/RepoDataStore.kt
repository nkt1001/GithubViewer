package com.apposum.data.repository

import com.apposum.domain.entity.DataEntity
import com.apposum.domain.entity.GithubReposEntity

interface RepoDataStore {
    suspend fun githubRepoEntityList(searchRequest: String, page: Int = 1): DataEntity<GithubReposEntity>
}