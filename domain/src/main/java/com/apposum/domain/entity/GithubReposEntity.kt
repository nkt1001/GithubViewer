package com.apposum.domain.entity

data class GithubReposEntity(val total: Int = 0,
                             val items: List<RepoEntity>? = emptyList(),
                             var nextPage: Int = 2)