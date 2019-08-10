package com.example.lesson12kislyakov

import android.app.Application
import android.content.Context
import com.example.lesson12kislyakov.di.component.ApplicationComponent
import com.example.lesson12kislyakov.di.component.DaggerApplicationComponent
import com.example.lesson12kislyakov.di.module.ApplicationModule

class BaseApp : Application() {
    private var mApplicationComponent: ApplicationComponent? = null

    // Needed to replace the component with a test specific one
    var component: ApplicationComponent?
        get() {
            if (mApplicationComponent == null) {
                mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(ApplicationModule(this))
                    .build()
            }
            return mApplicationComponent
        }
        set(applicationComponent) {
            mApplicationComponent = applicationComponent
        }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {

        operator fun get(context: Context): BaseApp {
            return context.applicationContext as BaseApp
        }
    }
}
