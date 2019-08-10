package com.example.lesson12kislyakov.di.module

import android.app.Application
import android.content.Context
import com.example.lesson12kislyakov.data.remote.NetworkService
import com.example.lesson12kislyakov.di.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class ApplicationModule(private val mApplication: Application) {

    @Provides
    internal fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return mApplication
    }

    @Provides
    @Singleton
    internal fun provideBridgesProvider(): NetworkService {
        return NetworkService.getRetrofit()
    }

}