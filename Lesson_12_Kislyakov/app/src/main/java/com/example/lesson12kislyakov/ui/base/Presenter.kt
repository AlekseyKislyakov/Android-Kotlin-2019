package com.example.lesson12kislyakov.ui.base

interface Presenter<V : MvpView> {
    fun attachView(mvpView: V)

    fun detachView()
}
