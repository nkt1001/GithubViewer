package com.apposum.data.repository

import com.apposum.data.api.GithubApi
import com.apposum.data.entity.RepoDataEntityMapper
import com.apposum.domain.entity.DataEntity
import com.apposum.domain.entity.ErrorEntity
import com.apposum.domain.entity.GithubReposEntity

class RemoteRepoDataStore(private val githubApi: GithubApi,
                          private val dataEntityMapper: RepoDataEntityMapper): RepoDataStore {

    override suspend fun githubRepoEntityList(searchRequest: String, page: Int): DataEntity<GithubReposEntity> = try {
        val result = githubApi.findRepos(searchRequest, page).items
        val nextPage = if (result.isNotEmpty()) { page + 1 } else { page }
        DataEntity.Success(dataEntityMapper.mapToEntity(result, nextPage))
    } catch (t: Throwable) {
        t.printStackTrace()
        DataEntity.Error(ErrorEntity(t.message))
    }
}