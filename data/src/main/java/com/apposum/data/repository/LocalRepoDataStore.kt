package com.apposum.data.repository

import com.apposum.data.db.GithubReposDao
import com.apposum.data.entity.GithubReposSearchData
import com.apposum.data.entity.RepoDataEntityMapper
import com.apposum.data.entity.RepoEntityDataMapper
import com.apposum.domain.entity.DataEntity
import com.apposum.domain.entity.GithubReposEntity
import com.apposum.domain.entity.GithubReposSearchEntity

class LocalRepoDataStore(private val reposDao: GithubReposDao,
                         private val entityToDataMapper: RepoEntityDataMapper,
                         private val dataToEntityMapper: RepoDataEntityMapper): RepoDataStore {

    override suspend fun githubRepoEntityList(searchRequest: String, page: Int): DataEntity<GithubReposEntity> {
        val cachedSearch = reposDao.searchPage(searchRequest, page) ?: return DataEntity.Success(GithubReposEntity(nextPage = page, searchQuery = searchRequest))

        return reposDao.loadById(cachedSearch.repo_ids).let { DataEntity.Success(dataToEntityMapper.mapToEntity(it,cachedSearch.page + 1, searchRequest)) }
    }

    suspend fun getSearchHistory(): List<GithubReposSearchEntity> = reposDao.getSearchHistory().map { GithubReposSearchEntity(it.search_request, it.repo_ids, it.page) }

    suspend fun findCacheRepoEntityList(searchRequest: String, page: Int): GithubReposEntity {
        val cachedSearch = reposDao.searchPage(searchRequest, page) ?: return GithubReposEntity(nextPage = page, searchQuery = searchRequest)

        return reposDao.loadById(cachedSearch.repo_ids).let { dataToEntityMapper.mapToEntity(it, cachedSearch.page + 1, searchRequest) }
    }

    suspend fun saveGithubRepoEntityList(searchRequest: String, page: Int, reposList: DataEntity<GithubReposEntity>) {
        entityToDataMapper.mapResponseToData(reposList)?.let { repos ->
            val repoIds = repos.map { it.id }
            reposDao.insertLastSearch(
                GithubReposSearchData(
                    page = page,
                    search_request = searchRequest,
                    repo_ids = repoIds
                )
            )
            reposDao.insertRepos(repos)
        }
    }
}