package com.example.lesson12kislyakov.di.component

import com.example.lesson12kislyakov.di.PerActivity
import com.example.lesson12kislyakov.di.module.ActivityModule
import com.example.lesson12kislyakov.ui.bridgedetail.BridgeDetailActivity
import com.example.lesson12kislyakov.ui.bridgelist.BridgeListActivity
import com.example.lesson12kislyakov.ui.bridgemap.MapsActivity
import dagger.Subcomponent

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(bridgeListActivity: BridgeListActivity)

    fun inject(bridgeDetailActivity: BridgeDetailActivity)

    fun inject(mapsActivity: MapsActivity)

}
