package com.example.lesson12kislyakov.di.component

import com.example.lesson12kislyakov.di.ConfigPersistent
import com.example.lesson12kislyakov.di.module.ActivityModule
import dagger.Component

@ConfigPersistent
@Component(dependencies = [ApplicationComponent::class])
interface ConfigPersistentComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent

}