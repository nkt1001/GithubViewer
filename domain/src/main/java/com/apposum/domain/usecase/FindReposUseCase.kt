package com.apposum.domain.usecase

import com.apposum.domain.entity.DataEntity
import com.apposum.domain.entity.GithubReposEntity
import com.apposum.domain.repository.GithubRepoRepository

class FindReposUseCase(private val githubRepoRepository: GithubRepoRepository) {

    suspend operator fun invoke(searchQuery: String, page: Int): DataEntity<GithubReposEntity> = githubRepoRepository.findRepos(searchQuery, page)
}