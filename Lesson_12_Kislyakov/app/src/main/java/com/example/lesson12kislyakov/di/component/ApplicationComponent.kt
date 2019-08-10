package com.example.lesson12kislyakov.di.component

import android.app.Application
import android.content.Context
import com.example.lesson12kislyakov.di.ApplicationContext
import com.example.lesson12kislyakov.di.module.ApplicationModule
import com.example.lesson12kislyakov.domain.repository.BridgesProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(bridgesProvider: BridgesProvider)

    @ApplicationContext
    fun context(): Context

    fun application(): Application
    fun bridgesProvider(): BridgesProvider
}