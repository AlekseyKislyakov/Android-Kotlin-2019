package com.example.lesson12kislyakov.ui.bridgelist

import com.example.lesson12kislyakov.data.models.Bridge
import com.example.lesson12kislyakov.ui.base.MvpView

interface BridgeListMvpView : MvpView {
    fun showLoadingError()
    fun showBridges(bridges: List<Bridge>)
    fun showSingleBridge(bridge: Bridge)
    fun showProgressView()
}