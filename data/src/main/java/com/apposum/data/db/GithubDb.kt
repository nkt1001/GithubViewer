package com.apposum.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apposum.data.entity.GithubReposSearchData
import com.apposum.data.entity.RepoData

@Database(entities = [RepoData::class, GithubReposSearchData::class], version = 1)
@TypeConverters(Converters::class)
abstract class GithubDb : RoomDatabase() {
    abstract fun getReposDao(): GithubReposDao
}