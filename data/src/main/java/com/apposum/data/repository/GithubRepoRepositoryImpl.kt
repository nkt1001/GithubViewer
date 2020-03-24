package com.apposum.data.repository

import com.apposum.domain.entity.DataEntity
import com.apposum.domain.entity.ErrorEntity
import com.apposum.domain.entity.GithubReposEntity
import com.apposum.domain.repository.GithubRepoRepository

class GithubRepoRepositoryImpl(private val remoteStore: RemoteRepoDataStore,
                               private val localStore: LocalRepoDataStore): GithubRepoRepository {

    override suspend fun findRepos(searchQuery: String, page: Int): DataEntity<GithubReposEntity> {
        try {
            when(val remoteResult = remoteStore.githubRepoEntityList(searchQuery, page)) {
                is DataEntity.Success -> localStore.saveGithubRepoEntityList(searchQuery, remoteResult)
                is DataEntity.Error -> return remoteResult.apply { data = localStore.findCacheRepoEntityList(searchQuery) }
            }
            return localStore.githubRepoEntityList(searchQuery, page)
        } catch (t: Throwable) {
            return DataEntity.Error(ErrorEntity(t.message))
        }
    }
}