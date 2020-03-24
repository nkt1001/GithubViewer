package com.apposum.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repo_search_results")
data class GithubReposSearchData(@PrimaryKey(autoGenerate = true) val index: Int = 0,
                                 val search_request: String,
                                 val repo_ids: List<Int>)