package com.apposum.githubrepos.reposearch

import androidx.lifecycle.MutableLiveData
import com.apposum.domain.usecase.FindReposUseCase
import com.apposum.githubrepos.common.BaseViewModel
import com.apposum.githubrepos.entity.Data
import com.apposum.githubrepos.entity.GithubReposSearch
import com.apposum.githubrepos.mapper.RepoEntityMapper

class RepoSearchViewModel(private val findReposUseCase: FindReposUseCase, private val mapper: RepoEntityMapper) : BaseViewModel() {
    var mNews = MutableLiveData<Data<GithubReposSearch>>()
}