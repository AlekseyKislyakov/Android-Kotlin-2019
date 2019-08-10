package com.example.lesson12kislyakov.domain.repository

import com.example.lesson12kislyakov.data.models.Bridge
import com.example.lesson12kislyakov.data.remote.NetworkService
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BridgesProvider @Inject
internal constructor(private val networkService: NetworkService) {

    val getBridges: Single<List<Bridge>>
        get() = networkService.getSingleBridgeResponse()
            .map { bridgeResponse -> bridgeResponse.objects}

    fun getSingleBridge(bridgeId: Int): Single<Bridge> {
        return networkService.getOneSingleBridge(bridgeId)
    }


}
