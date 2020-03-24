package com.apposum.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.apposum.data.api.GithubApi
import com.apposum.data.db.GithubDb
import com.apposum.data.entity.RepoDataEntityMapper
import com.apposum.data.entity.RepoEntityDataMapper
import com.apposum.domain.repository.GithubRepoRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class RemoteRepoDataStoreTest {

    private val mockWebServer = MockWebServer()
    private lateinit var db: GithubDb
    private lateinit var repository: GithubRepoRepository

    private val dataEntityMapper = RepoDataEntityMapper()
    private val entityDataMapper = RepoEntityDataMapper()

    @Before
    fun setup() {
        mockWebServer.start()

        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GithubDb::class.java).build()

        val apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(Json(
                JsonConfiguration(encodeDefaults = false,
                strictMode = false,
                useArrayPolymorphism = true)
            )
                .asConverterFactory("application/json; charset=utf-8".toMediaType()))
            .client(OkHttpClient())
            .build()
            .create(GithubApi::class.java)

        val remoteStore = RemoteRepoDataStore(apiService, dataEntityMapper)
        val localStore = LocalRepoDataStore(db.getReposDao(), entityDataMapper, dataEntityMapper)

        repository = GithubRepoRepositoryImpl(remoteStore, localStore)
    }
    @After
    @Throws(IOException::class)
    fun end() {
        mockWebServer.shutdown()
        db.close()
    }

    @Test
    fun httpOkRequestAndSaveResult() {

    }

    @Test
    fun twoPagesHttpOkRequestAndSaveResult() {

    }

    @Test
    fun httpOkRequestThenHttpFailedAndSaveResult() {

    }

    @Test
    fun httpFailedRequest() {

    }

}