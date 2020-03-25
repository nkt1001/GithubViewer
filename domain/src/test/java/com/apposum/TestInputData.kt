package com.apposum

import com.apposum.domain.entity.GithubReposEntity
import com.apposum.domain.entity.OwnerEntity
import com.apposum.domain.entity.RepoEntity

internal val dummyRepoList: List<RepoEntity> = listOf(RepoEntity(
    id = 1,
    name = "Foo",
    fullName = "Buzz",
    description = "Some description 1",
    owner = OwnerEntity("User 1", "awesomeapposum.com"),
    stars = 5
), RepoEntity(
    id = 2,
    name = "FooFoo",
    fullName = "BuzzBuzz",
    description = "Some description 2",
    owner = OwnerEntity("User 2", "awesomeapposum.com"),
    stars = 5
), RepoEntity(
    id = 3,
    name = "FooFooFoo",
    fullName = "BuzzBuzzBuzz",
    description = "Some description 3",
    owner = OwnerEntity("User 3", "awesomeapposum.com"),
    stars = 5
), RepoEntity(
    id = 4,
    name = "FooFooFooFoo",
    fullName = "BuzzBuzzBuzzBuzz",
    description = "Some description 4",
    owner = OwnerEntity("User 4", "awesomeapposum.com"),
    stars = 5
), RepoEntity(
    id = 5,
    name = "FooFooFooFooFoo",
    fullName = "BuzzBuzzBuzzBuzzBuzz",
    description = "Some description 5",
    owner = OwnerEntity("User 5", "awesomeapposum.com"),
    stars = 5
))

internal val dummySearchResult: GithubReposEntity = GithubReposEntity(
    total = dummyRepoList.size,
    items = dummyRepoList,
    nextPage = 2
)