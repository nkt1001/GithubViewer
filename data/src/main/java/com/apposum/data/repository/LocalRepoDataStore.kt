package com.apposum.data.repository

import com.apposum.data.db.GithubReposDao
import com.apposum.data.entity.GithubReposSearchData
import com.apposum.data.entity.RepoDataEntityMapper
import com.apposum.data.entity.RepoEntityDataMapper
import com.apposum.domain.entity.DataEntity
import com.apposum.domain.entity.GithubReposEntity

class LocalRepoDataStore(private val reposDao: GithubReposDao,
                         private val entityToDataMapper: RepoEntityDataMapper,
                         private val dataToEntityMapper: RepoDataEntityMapper): RepoDataStore {

    override suspend fun githubRepoEntityList(searchRequest: String, page: Int): DataEntity<GithubReposEntity> {
        val cachedSearch = reposDao.search(searchRequest)
        val repoIds = cachedSearch.flatMap {
            it.repo_ids
        }

        return reposDao.loadById(repoIds).let { DataEntity.Success(dataToEntityMapper.mapToEntity(it, cachedSearch.size + 1)) }
    }

    suspend fun findCacheRepoEntityList(searchRequest: String): GithubReposEntity {
        val cachedSearch = reposDao.search(searchRequest)
        val repoIds = cachedSearch.flatMap {
            it.repo_ids
        }

        return reposDao.loadById(repoIds).let { dataToEntityMapper.mapToEntity(it, cachedSearch.size + 1) }
    }

    suspend fun saveGithubRepoEntityList(searchRequest: String, reposList: DataEntity<GithubReposEntity>) {
        entityToDataMapper.mapResponseToData(reposList)?.let { repos ->
            val repoIds = repos.map { it.id }
            reposDao.insertLastSearch(GithubReposSearchData(search_request = searchRequest, repo_ids = repoIds))
            reposDao.insertRepos(repos)
        }
    }
}