package com.apposum.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apposum.data.entity.GithubReposSearchData
import com.apposum.data.entity.RepoData

@Dao
interface GithubReposDao {

    @Query("Select * from repo_data")
    fun getAllRepos(): List<RepoData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(repositories: List<RepoData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLastSearch(data: GithubReposSearchData)

    @Query("SELECT * FROM repo_search_results WHERE search_request = :query AND page = :page")
    suspend fun searchPage(query: String, page: Int): GithubReposSearchData?

    @Query("SELECT * FROM repo_search_results WHERE search_request = :query")
    suspend fun searchAll(query: String): List<GithubReposSearchData>

    @Query("SELECT * FROM repo_search_results ORDER BY request_time DESC")
    suspend fun getSearchHistory(): List<GithubReposSearchData>

    @Query("SELECT * FROM repo_data WHERE id IN (:repoIds)")
    suspend fun loadById(repoIds: List<Int>): List<RepoData>

    @Query("DELETE FROM repo_data")
    suspend fun clearRepoData()

    @Query("DELETE FROM repo_search_results")
    suspend fun clearSearchData()
}