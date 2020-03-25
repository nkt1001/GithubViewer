package com.apposum.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
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

class RepoDataEntityMapper {

    fun mapToEntity(mapArticles: List<RepoData>?, nextPage: Int = 0) = mapListReposToEntity(mapArticles).let {
        GithubReposEntity(items = it,
            total = it.size,
            nextPage = nextPage)
    }

    fun mapListReposToEntity(repos: List<RepoData>?): List<RepoEntity> = repos?.map { mapRepoToEntity(it) } ?: emptyList()

    fun mapRepoToEntity(response: RepoData): RepoEntity = RepoEntity(
        id = response.id,
        name = response.name,
        fullName = response.full_name,
        description = response.description,
        owner = OwnerEntity(userName = response.owner.login, url = response.owner.url),
        stars = response.stargazers_count
    )
}

class RepoEntityDataMapper {

    fun mapRepoListToData(entity: List<RepoEntity>): List<RepoData> = entity.map {
        mapRepoToData(it)
    }

    fun mapRepoToData(entity: RepoEntity): RepoData = RepoData(
        id = entity.id,
        name = entity.name,
        full_name = entity.fullName,
        description = entity.description,
        owner = OwnerData(login = entity.owner.userName, url = entity.owner.url),
        stargazers_count = entity.stars
    )

    fun mapResponseToData(entity: DataEntity<GithubReposEntity>): List<RepoData>? {
        return when (entity) {
            is DataEntity.Success<GithubReposEntity> -> entity.data?.items?.map { mapRepoToData(it) }
            is DataEntity.Error<GithubReposEntity> -> entity.data?.items?.map { mapRepoToData(it) }
            is DataEntity.Loading<GithubReposEntity> -> entity.data?.items?.map { mapRepoToData(it) }
        }
    }
}