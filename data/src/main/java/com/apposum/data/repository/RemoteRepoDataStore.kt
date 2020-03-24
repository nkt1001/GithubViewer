package com.apposum.data.repository

import com.apposum.data.api.GithubApi
import com.apposum.data.entity.RepoDataEntityMapper
import com.apposum.domain.entity.DataEntity
import com.apposum.domain.entity.ErrorEntity
import com.apposum.domain.entity.GithubReposEntity

class RemoteRepoDataStore(private val githubApi: GithubApi,
                          private val dataEntityMapper: RepoDataEntityMapper): RepoDataStore {

    override suspend fun githubRepoEntityList(searchRequest: String, page: Int): DataEntity<GithubReposEntity> = try {
        DataEntity.Success(dataEntityMapper.mapToEntity(githubApi.findRepos(searchRequest, page).items, page + 1))
    } catch (t: Throwable) {
        DataEntity.Error(ErrorEntity(t.message))
    }
}