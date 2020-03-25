package com.apposum.githubrepos.entity

data class GithubRepos(var nextPage: Int,
                       val total: Int = 0,
                       val items: List<Repo>? = emptyList())