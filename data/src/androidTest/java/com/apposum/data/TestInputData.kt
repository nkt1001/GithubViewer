package com.apposum.data

import com.apposum.data.entity.GithubReposData
import com.apposum.data.entity.OwnerData
import com.apposum.data.entity.RepoData

internal val dummyRepoDataList: List<RepoData> = listOf(RepoData(
    id = 1,
    name = "Foo",
    full_name = "Buzz",
    description = "Some description 1",
    owner = OwnerData("User 1", "awesomeapposum.com"),
    stargazers_count = 5
), RepoData(
    id = 2,
    name = "FooFoo",
    full_name = "BuzzBuzz",
    description = "Some description 2",
    owner = OwnerData("User 2", "awesomeapposum.com"),
    stargazers_count = 5
), RepoData(
    id = 3,
    name = "FooFooFoo",
    full_name = "BuzzBuzzBuzz",
    description = "Some description 3",
    owner = OwnerData("User 3", "awesomeapposum.com"),
    stargazers_count = 5
), RepoData(
    id = 4,
    name = "FooFooFooFoo",
    full_name = "BuzzBuzzBuzzBuzz",
    description = "Some description 4",
    owner = OwnerData("User 4", "awesomeapposum.com"),
    stargazers_count = 5
), RepoData(
    id = 5,
    name = "FooFooFooFooFoo",
    full_name = "BuzzBuzzBuzzBuzzBuzz",
    description = "Some description 5",
    owner = OwnerData("User 5", "awesomeapposum.com"),
    stargazers_count = 5
))

internal val dummySearchDataResult: GithubReposData = GithubReposData(
    dummyRepoDataList.size,
    dummyRepoDataList
)

