package com.apposum.githubrepos.di

import androidx.room.Room
import com.apposum.data.api.GithubApi
import com.apposum.data.db.GithubDb
import com.apposum.data.entity.RepoDataEntityMapper
import com.apposum.data.entity.RepoEntityDataMapper
import com.apposum.data.repository.GithubRepoRepositoryImpl
import com.apposum.data.repository.LocalRepoDataStore
import com.apposum.data.repository.RemoteRepoDataStore
import com.apposum.domain.repository.GithubRepoRepository
import com.apposum.domain.usecase.FindReposUseCase
import com.apposum.githubrepos.BuildConfig
import com.apposum.githubrepos.mapper.RepoEntityMapper
import com.apposum.githubrepos.reposearch.RepoSearchViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val mRepositoryModules = module {
    single(named("remote")) { RemoteRepoDataStore(githubApi = get(named(API)), dataEntityMapper = get())}
    single(named("local")) { LocalRepoDataStore(
        reposDao = get<GithubDb>(named(DATABASE)).getReposDao(),
        entityToDataMapper = get(),
        dataToEntityMapper = get()
    )
    }
    single<GithubRepoRepository> { GithubRepoRepositoryImpl(remoteStore = get(named("remote")), localStore = get(named("local"))) }
    single { RepoEntityDataMapper() }
    single { RepoDataEntityMapper() }
    single { RepoEntityMapper() }
}

val mUseCaseModules = module {
    factory(named(GET_NEWS_USECASE)) { FindReposUseCase(get()) }
}

val mNetworkModules = module {

    single(named(JSON_SERIALIZER_INSTANCE)) {
        Json {
            encodeDefaults = false
            useArrayPolymorphism = true
            ignoreUnknownKeys = true
            serializeSpecialFloatingPointValues = true
        }.asConverterFactory("application/json; charset=utf-8".toMediaType())
    }
    single(named(OKHTTP_INSTANCE)) { OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(get<HttpLoggingInterceptor>(named("HttpLoggingInterceptor")))
            }
        }
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .build()
    }
    single(named(RETROFIT_INSTANCE)) {
        Retrofit.Builder()
            .client(get<OkHttpClient>(named(OKHTTP_INSTANCE)))
            .baseUrl(BASE_URL.toHttpUrl())
            .addConverterFactory(get<Converter.Factory>(named(JSON_SERIALIZER_INSTANCE)))
            .build()
    }
    single(named(API)) { get<Retrofit>(named(RETROFIT_INSTANCE)).create(GithubApi::class.java) }
}

val mLocalModules = module {
    single(named(DATABASE)) { Room.databaseBuilder(androidApplication(), GithubDb::class.java, "github.db").build() }
}

val mViewModels = module {
    viewModel { RepoSearchViewModel(get(), get()) }
}

private const val RETROFIT_INSTANCE = "Retrofit"
private const val OKHTTP_INSTANCE = "OkHttp"
private const val JSON_SERIALIZER_INSTANCE = "Serializer"
private const val API = "Api"
private const val GET_NEWS_USECASE = "getNewsUseCase"
private const val DATABASE = "database"
private const val BASE_URL = "https://api.github.com/"