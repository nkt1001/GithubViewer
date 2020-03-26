package com.apposum.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.apposum.data.api.GithubApi
import com.apposum.data.db.GithubDb
import com.apposum.data.entity.RepoDataEntityMapper
import com.apposum.data.entity.RepoEntityDataMapper
import com.apposum.domain.entity.DataEntity
import com.apposum.domain.repository.GithubRepoRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class GithubRepoRepositoryTest {

    private val mockWebServer = MockWebServer()
    private lateinit var db: GithubDb
    private lateinit var repository: GithubRepoRepository
    private lateinit var localStore: LocalRepoDataStore

    private val dataEntityMapper = RepoDataEntityMapper()
    private val entityDataMapper = RepoEntityDataMapper()

    @Before
    fun setup() {
        mockWebServer.start()

        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GithubDb::class.java).build()

        val apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("").toString())
            .addConverterFactory(Json {
                encodeDefaults = false
                useArrayPolymorphism = true
                ignoreUnknownKeys = true
                serializeSpecialFloatingPointValues = true
            }.asConverterFactory("application/json; charset=utf-8".toMediaType()))
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor())
                .build())
            .build()
            .create(GithubApi::class.java)

        val remoteStore = RemoteRepoDataStore(apiService, dataEntityMapper)
        localStore = LocalRepoDataStore(db.getReposDao(), entityDataMapper, dataEntityMapper)

        repository = GithubRepoRepositoryImpl(remoteStore, localStore)
    }
    @After
    @Throws(IOException::class)
    fun end() {
        mockWebServer.shutdown()
        db.close()
    }

    @Test
    fun httpOkRequestAndSaveResult() = runBlocking(Dispatchers.IO) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val input = context.assets.open("okResponse")


        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(Buffer().readFrom(input))
        })
        input.close()
        val response = repository.findRepos("httpOkRequestAndSaveResult", 1)

        Assert.assertTrue(response is DataEntity.Success)

        val reposInDb = localStore.findCacheRepoEntityList("httpOkRequestAndSaveResult", 1)

        Assert.assertTrue(reposInDb?.items?.size == 30)
    }


    @Test
    fun twoPagesHttpOkRequestAndSaveResult() = runBlocking(Dispatchers.IO) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val input = context.assets.open("okResponse")
        val input2 = context.assets.open("okResponse2")

        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(Buffer().readFrom(input))
        })

        mockWebServer.enqueue(MockResponse().apply {
            setBody(Buffer().readFrom(input2))
            setResponseCode(200)
        })

        val response = repository.findRepos("twoPagesHttpOkRequestAndSaveResult", 1)
        val reposInDb = localStore.findCacheRepoEntityList("twoPagesHttpOkRequestAndSaveResult", 1)

        Assert.assertTrue(response is DataEntity.Success)
        Assert.assertTrue((response as DataEntity.Success).data?.items != null)
        Assert.assertTrue(reposInDb.items?.size == response.data?.items?.size)


        val response2 = repository.findRepos("twoPagesHttpOkRequestAndSaveResult", response.data?.nextPage ?: 0)
        val reposInDb2 = localStore.findCacheRepoEntityList("twoPagesHttpOkRequestAndSaveResult", response.data?.nextPage ?: 0)
        Assert.assertTrue(response2 is DataEntity.Success)
        Assert.assertTrue((response2 as DataEntity.Success).data?.items != null)
        Assert.assertTrue(reposInDb2.items?.size == response2.data?.items?.size)
    }

    @Test
    fun httpOkRequestThenHttpFailedAndSaveResult() = runBlocking(Dispatchers.IO) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val input = context.assets.open("okResponse")
        val input2 = context.assets.open("failedResponse")

        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(Buffer().readFrom(input))
        })

        mockWebServer.enqueue(MockResponse().apply {
            setBody(Buffer().readFrom(input2))
            setResponseCode(422)
        })

        val response = repository.findRepos("httpOkRequestThenHttpFailedAndSaveResult", 1)
        val reposInDb = localStore.findCacheRepoEntityList("httpOkRequestThenHttpFailedAndSaveResult", 1)

        Assert.assertTrue(response is DataEntity.Success)
        Assert.assertTrue((response as DataEntity.Success).data?.items != null)
        Assert.assertTrue(reposInDb.items?.size == response.data?.items?.size)


        val response2 = repository.findRepos("httpOkRequestThenHttpFailedAndSaveResult", response.data?.nextPage ?: 0)
        Assert.assertTrue(response2 is DataEntity.Error)
        Assert.assertTrue((response2 as DataEntity.Error).data?.items?.isEmpty() ?: false)
    }

    @Test
    fun emptyRespondThenOk() = runBlocking(Dispatchers.IO) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val input = context.assets.open("okResponseEmpty")
        val input2 = context.assets.open("okResponse")

        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(Buffer().readFrom(input))
        })

        mockWebServer.enqueue(MockResponse().apply {
            setBody(Buffer().readFrom(input2))
            setResponseCode(200)
        })

        val response = repository.findRepos("emptyRespondThenOk", 1)
        val reposInDb = localStore.findCacheRepoEntityList("emptyRespondThenOk", 1)

        Assert.assertTrue(response is DataEntity.Success)
        Assert.assertTrue((response as DataEntity.Success).data?.items != null)
        Assert.assertTrue(reposInDb.items?.size == response.data?.items?.size)


        val response2 = repository.findRepos("emptyRespondThenOk", response.data?.nextPage ?: 0)
        val reposInDb2 = localStore.findCacheRepoEntityList("emptyRespondThenOk", response.data?.nextPage ?: 0)
        val searchHistory = localStore.getSearchHistory()

        Assert.assertTrue(response2 is DataEntity.Success)
        Assert.assertTrue((response2 as DataEntity.Success).data?.items != null)
        Assert.assertTrue(reposInDb2.items?.size == response.data?.items?.size?.plus(response2.data?.items?.size ?: 0))
        Assert.assertTrue(searchHistory.size == 2)
        Assert.assertTrue(searchHistory[0].query == "emptyRespondThenOk")
        Assert.assertTrue(searchHistory[0].page == 2)
    }

    @Test
    fun httpFailedRequest() = runBlocking(Dispatchers.IO) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val input = context.assets.open("failedResponse")


        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(422)
            setBody(Buffer().readFrom(input))
        })
        input.close()
        val response = repository.findRepos("httpFailedRequest", 1)
        val reposInDb = localStore.findCacheRepoEntityList("httpFailedRequest", 1)

        Assert.assertTrue(response is DataEntity.Error)
        Assert.assertTrue(reposInDb.items?.isEmpty() ?: false)
    }
    @Test
    fun findReposThanGetLastSearch() = runBlocking(Dispatchers.IO) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val input = context.assets.open("okResponse")


        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(Buffer().readFrom(input))
        })
        input.close()
        val response = repository.findRepos("httpOkRequestAndSaveResult", 1)

        Assert.assertTrue(response is DataEntity.Success)

        val reposInDb = localStore.findCacheRepoEntityList("httpOkRequestAndSaveResult", 1)

        Assert.assertTrue(reposInDb.items?.size == 30)

        val lastSearch = repository.getLastSearch()
        Assert.assertTrue(response.data == lastSearch.data)
    }
}
