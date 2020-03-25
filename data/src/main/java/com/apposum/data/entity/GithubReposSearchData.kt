package com.apposum.data.entity

import androidx.room.Entity

@Entity(tableName = "repo_search_results", primaryKeys = ["page", "search_request"])
data class GithubReposSearchData(val page: Int = 1,
                                 val search_request: String,
                                 val repo_ids: List<Int>,
                                 val request_time: Long = System.currentTimeMillis())