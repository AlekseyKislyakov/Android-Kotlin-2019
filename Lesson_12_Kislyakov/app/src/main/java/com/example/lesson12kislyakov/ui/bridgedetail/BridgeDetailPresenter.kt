package com.example.lesson12kislyakov.ui.bridgedetail

import com.example.lesson12kislyakov.data.models.Bridge
import com.example.lesson12kislyakov.domain.repository.BridgesProvider
import com.example.lesson12kislyakov.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BridgeDetailPresenter @Inject
constructor(private val mBridgesProvider: BridgesProvider) : BasePresenter<BridgeDetailMvpView>() {
    private var mDisposable: Disposable? = null

    override fun attachView(mvpView: BridgeDetailMvpView) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        if (mDisposable != null) mDisposable!!.dispose()
    }

    fun loadSingleBridge(bridgeId: Int) {
        checkViewAttached()
        mvpView?.showProgressView()
        mDisposable = mBridgesProvider.getSingleBridge(bridgeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bridge: Bridge -> mvpView?.showSingleBridge(bridge) },
                        { error ->
                            error.printStackTrace()
                            mvpView?.showLoadingError()
                        })
    }

}