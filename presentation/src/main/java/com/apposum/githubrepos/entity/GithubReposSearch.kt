package com.apposum.githubrepos.entity

data class GithubReposSearch(val query: String,
                             val items: List<Int>? = emptyList(),
                             var page: Int = 0)