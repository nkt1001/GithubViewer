package com.apposum.githubrepos

import android.app.Application
import com.apposum.githubrepos.di.*
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            androidFileProperties()
            modules(listOf(mNetworkModules,
                mViewModels,
                mRepositoryModules,
                mUseCaseModules,
                mLocalModules))
        }
    }
}