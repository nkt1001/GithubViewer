package com.apposum.githubrepos.reposearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apposum.domain.usecase.FindReposUseCase
import com.apposum.domain.usecase.GetLastSearchResultUseCase
import com.apposum.githubrepos.common.BaseViewModel
import com.apposum.githubrepos.entity.Data
import com.apposum.githubrepos.entity.Error
import com.apposum.githubrepos.entity.GithubRepos
import com.apposum.githubrepos.mapper.RepoEntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepoSearchViewModel(private val findReposUseCase: FindReposUseCase,
                          private val getLastSearchUseCase: GetLastSearchResultUseCase,
                          private val mapper: RepoEntityMapper) : BaseViewModel() {

    var lastSearchResult: GithubRepos? = null
        private set
    private val repos = MutableLiveData<Data<GithubRepos>>()

    val reposLiveData: LiveData<Data<GithubRepos>>
        get() = repos

    fun findGithubRepository(searchQuery: String) {

        if (searchQuery == lastSearchResult?.searchQuery || searchQuery.isBlank()) {
            repos.postValue(Data.Error(Error("Invalid request")))
        }

        launch {
            repos.postValue(Data.Loading(true))
            val searchResult =  withContext(Dispatchers.IO) { mapper.mapFrom(findReposUseCase(searchQuery, 1)) }
            lastSearchResult = searchResult.data
            repos.postValue(searchResult)
            repos.postValue(Data.Loading(false))
        }
    }

    fun loadNextPage() {
        lastSearchResult?.let {
            launch {
                repos.postValue(Data.Loading(true))
                val searchResult = withContext(Dispatchers.IO) { mapper.mapFrom(findReposUseCase(it.searchQuery, it.nextPage)) }
                lastSearchResult = searchResult.data
                repos.postValue(searchResult)
                repos.postValue(Data.Loading(false))
            }
        }
    }

    fun loadLastSearchResult() {
        launch {
            repos.postValue(Data.Loading(true))
            val result = withContext(Dispatchers.Main) {  mapper.mapFrom(getLastSearchUseCase()) }
            lastSearchResult = result.data
            repos.postValue(result)
            repos.postValue(Data.Loading(false))
        }
    }
}