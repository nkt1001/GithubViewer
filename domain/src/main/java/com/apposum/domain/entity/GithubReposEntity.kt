package com.apposum.domain.entity

data class GithubReposEntity(var nextPage: Int,
                             val searchQuery: String,
                             val total: Int = 0,
                             val items: List<RepoEntity>? = emptyList())