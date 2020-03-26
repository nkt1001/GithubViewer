package com.apposum.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apposum.domain.common.Mapper
import com.apposum.domain.entity.DataEntity
import com.apposum.domain.entity.GithubReposEntity
import com.apposum.domain.entity.OwnerEntity
import com.apposum.domain.entity.RepoEntity
import kotlinx.serialization.Serializable

@Serializable
data class GithubReposData(val total_count: Int = 0,
                           val items: List<RepoData>)

@Entity(tableName = "repo_data")
@Serializable
data class RepoData(@PrimaryKey val id: Int,
                    val name: String,
                    val full_name: String,
                    val description: String?,
                    @Embedded val owner: OwnerData,
                    val stargazers_count: Int)

@Serializable
data class OwnerData(val login: String,
                     val url: String?)

class RepoDataEntityMapper : Mapper<RepoData, RepoEntity> {

    fun mapToEntity(mapRepos: List<RepoData>?, nextPage: Int = 0, searchQuery: String): GithubReposEntity = mapListReposToEntity(mapRepos).let {
        GithubReposEntity(items = it,
            total = it.size,
            nextPage = nextPage,
            searchQuery = searchQuery)
    }

    private fun mapListReposToEntity(repos: List<RepoData>?): List<RepoEntity> = repos?.map { mapFrom(it) } ?: emptyList()

    override fun mapFrom(from: RepoData): RepoEntity = RepoEntity(
        id = from.id,
        name = from.name,
        fullName = from.full_name,
        description = from.description,
        owner = OwnerEntity(userName = from.owner.login, url = from.owner.url),
        stars = from.stargazers_count
    )
}

class RepoEntityDataMapper : Mapper<RepoEntity, RepoData> {

    fun mapResponseToData(entity: DataEntity<GithubReposEntity>): List<RepoData>? {
        return when (entity) {
            is DataEntity.Success<GithubReposEntity> -> entity.data?.items?.map { mapFrom(it) }
            is DataEntity.Error<GithubReposEntity> -> entity.data?.items?.map { mapFrom(it) }
            is DataEntity.Loading<GithubReposEntity> -> entity.data?.items?.map { mapFrom(it) }
        }
    }

    override fun mapFrom(from: RepoEntity): RepoData = RepoData(
        id = from.id,
        name = from.name,
        full_name = from.fullName,
        description = from.description,
        owner = OwnerData(login = from.owner.userName, url = from.owner.url),
        stargazers_count = from.stars
    )
}