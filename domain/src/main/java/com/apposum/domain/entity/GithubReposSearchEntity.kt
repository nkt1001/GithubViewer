package com.apposum.domain.entity

data class GithubReposSearchEntity(val query: String,
                                   val items: List<Int>? = emptyList(),
                                   var page: Int = 0)