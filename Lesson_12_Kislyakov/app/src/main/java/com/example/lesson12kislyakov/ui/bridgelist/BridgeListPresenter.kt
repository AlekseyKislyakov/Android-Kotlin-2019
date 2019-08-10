package com.example.lesson12kislyakov.ui.bridgelist

import com.example.lesson12kislyakov.domain.repository.BridgesProvider
import com.example.lesson12kislyakov.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BridgeListPresenter @Inject
constructor(private val mBridgesProvider: BridgesProvider) : BasePresenter<BridgeListMvpView>() {
    private var mDisposable: Disposable? = null

    override fun attachView(mvpView: BridgeListMvpView) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        mDisposable?.dispose()
    }

    fun loadBridges() {
        checkViewAttached()
        mvpView?.showProgressView()
        mDisposable = mBridgesProvider.getBridges
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ bridges -> mvpView?.showBridges(bridges) },
                { error ->
                    error.printStackTrace()
                    mvpView?.showLoadingError()
                })
    }

}