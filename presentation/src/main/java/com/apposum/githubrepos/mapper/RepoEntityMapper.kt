package com.apposum.githubrepos.mapper

import com.apposum.domain.common.Mapper
import com.apposum.domain.entity.DataEntity
import com.apposum.domain.entity.GithubReposEntity
import com.apposum.domain.entity.RepoEntity
import com.apposum.githubrepos.entity.*

class RepoEntityMapper : Mapper<DataEntity<GithubReposEntity>, Data<GithubRepos>> {

    override fun mapFrom(from: DataEntity<GithubReposEntity>): Data<GithubRepos> {
        return when (from) {
            is DataEntity.Success -> Data.Success(from.data?.let { GithubRepos(it.nextPage, it.total, mapReposToPresentation(it.items)) })
            is DataEntity.Error -> Data.Error(Error(from.error.message), from.data?.let { GithubRepos(it.nextPage, it.total, mapReposToPresentation(it.items)) })
            is DataEntity.Loading -> Data.Loading()
        }
    }

    private fun mapReposToPresentation(items: List<RepoEntity>?): List<Repo> = items?.map { mapRepoFromEntity(it) } ?: emptyList()

    private fun mapRepoFromEntity(from: RepoEntity): Repo = Repo(id = from.id,
        owner = Owner(from.owner.userName, from.owner.url),
        stars = from.stars,
        fullName = from.fullName,
        description = from.description,
        name = from.name
    )
}