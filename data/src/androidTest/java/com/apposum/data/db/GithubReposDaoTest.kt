package com.apposum.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.apposum.data.dummyRepoDataList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class GithubReposDaoTest {
    private lateinit var db: GithubDb
    private lateinit var reposDao: GithubReposDao

    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GithubDb::class.java).build()
        reposDao = db.getReposDao()
    }

    @After
    @Throws(IOException::class)
    fun end(){
        db.close()
    }

    @Test
    fun addReposInDatabase() = runBlocking {
        reposDao.insertRepos(dummyRepoDataList)

        val ids = dummyRepoDataList.map { it.id }
        val repos = reposDao.loadById(ids)
        assert(repos.isNotEmpty())
    }

    @Test
    fun clearReposFromDatabase() = runBlocking {
        reposDao.insertRepos(dummyRepoDataList)

        reposDao.clearRepoData();
        val repos = reposDao.getAllRepos()
        assert(repos.isEmpty())
    }

    @Test
    fun writeReposAndReadRepo() = runBlocking {
        reposDao.insertRepos(dummyRepoDataList)
        val allRepos = reposDao.getAllRepos()
        assertThat(allRepos[0].name, CoreMatchers.equalTo("Foo"))
    }

    @Test
    fun findNonExistingItem() = runBlocking {
        reposDao.insertRepos(dummyRepoDataList)
        val searchHistory = reposDao.search("Foo")
        assert(searchHistory.isEmpty())
    }

}
