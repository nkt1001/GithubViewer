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
                is DataEntity.Success -> localStore.saveGithubRepoEntityList(searchQuery, page, remoteResult)
                is DataEntity.Error -> return remoteResult.apply { data = localStore.findCacheRepoEntityList(searchQuery, page) }
            }
            return localStore.githubRepoEntityList(searchQuery, page)
        } catch (t: Throwable) {
            return DataEntity.Error(ErrorEntity(t.message))
        }
    }

    override suspend fun getLastSearch(): DataEntity<GithubReposEntity> {
        try {
            val lastSearchResult = localStore.getSearchHistory()

            if (lastSearchResult.isNullOrEmpty()) {
                return DataEntity.Success()
            }

            when(val remoteResult = remoteStore.githubRepoEntityList(lastSearchResult[0].query, 1)) {
                is DataEntity.Success -> localStore.saveGithubRepoEntityList(lastSearchResult[0].query, 1, remoteResult)
                is DataEntity.Error -> return remoteResult.apply { data = localStore.findCacheRepoEntityList(lastSearchResult[0].query, 1) }
            }
            return localStore.githubRepoEntityList(lastSearchResult[0].query, 1)
        } catch (t: Throwable) {
            return DataEntity.Error(ErrorEntity(t.message))
        }
    }
}