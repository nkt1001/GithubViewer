package com.apposum.domain.usecase

import com.apposum.domain.entity.DataEntity
import com.apposum.domain.entity.GithubReposEntity
import com.apposum.domain.repository.GithubRepoRepository

class GetLastSearchResultUseCase(private val githubRepoRepository: GithubRepoRepository) {
    suspend operator fun invoke(): DataEntity<GithubReposEntity> = githubRepoRepository.getLastSearch()
}