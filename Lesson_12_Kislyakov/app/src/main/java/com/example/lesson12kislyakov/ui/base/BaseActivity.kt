package com.example.lesson12kislyakov.ui.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.LongSparseArray
import com.example.lesson12kislyakov.BaseApp
import com.example.lesson12kislyakov.di.component.ActivityComponent
import com.example.lesson12kislyakov.di.component.ConfigPersistentComponent
import com.example.lesson12kislyakov.di.component.DaggerConfigPersistentComponent
import com.example.lesson12kislyakov.di.module.ActivityModule
import java.util.concurrent.atomic.AtomicLong

open class BaseActivity : AppCompatActivity() {

    private var mActivityComponent: ActivityComponent? = null
    private var mActivityId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState?.getLong(KEY_ACTIVITY_ID) ?: NEXT_ID.getAndIncrement()

        var configPersistentComponent = sComponentsMap.get(mActivityId, null)

        if (configPersistentComponent == null) {
            Log.d(TAG, "onCreate: configPersistentComp")
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                .applicationComponent(BaseApp[this].component)
                .build()
            sComponentsMap.put(mActivityId, configPersistentComponent)
        }
        mActivityComponent = configPersistentComponent!!.activityComponent(ActivityModule(this))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_ACTIVITY_ID, mActivityId)
    }

    override fun onDestroy() {
        if (!isChangingConfigurations) {
            Log.d(TAG, "onDestroy: Clearing ConfigPersistentComponent id=$mActivityId")
            sComponentsMap.remove(mActivityId)
        }
        super.onDestroy()
    }

    fun activityComponent(): ActivityComponent? {
        return mActivityComponent
    }

    companion object {

        private val TAG = "myTag"

        private val KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID"
        private val NEXT_ID = AtomicLong(0)
        private val sComponentsMap = LongSparseArray<ConfigPersistentComponent>()
    }

}