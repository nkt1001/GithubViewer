package com.apposum.domain

import com.apposum.domain.entity.DataEntity
import com.apposum.domain.entity.GithubReposEntity
import com.apposum.domain.repository.GithubRepoRepository
import com.apposum.domain.usecase.FindReposUseCase
import com.apposum.dummySearchResult
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FindReposUseCaseTest {
    @Mock
    lateinit var repository: GithubRepoRepository

    lateinit var useCaseTest: FindReposUseCase

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        useCaseTest = FindReposUseCase(repository)
    }

    @Test
    fun `data from the repository`() = runBlocking {
        getGithubReposEntityInResult(dummySearchResult)
        val result = repository.findRepos()
        assert((result as? DataEntity.Success)?.data?.items?.isNotEmpty() ?: false)
    }

    @Test
    fun `data from the repository is null`() = runBlocking {
        getGithubReposEntityInResult(GithubReposEntity())
        val result = repository.findRepos()

        assert((result as? DataEntity.Success)?.data?.items.isNullOrEmpty())
    }

    private suspend fun getGithubReposEntityInResult(testDataEntity: GithubReposEntity) {
        Mockito.`when`(repository.findRepos()).thenReturn(DataEntity.Success(testDataEntity))
    }
}