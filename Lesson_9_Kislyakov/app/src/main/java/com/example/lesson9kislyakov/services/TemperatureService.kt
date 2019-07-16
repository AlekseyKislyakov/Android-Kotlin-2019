package com.example.lesson9kislyakov.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.lesson9kislyakov.utils.ServiceCallbacks
import com.example.lesson9kislyakov.network.WeatherApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class TemperatureService : Service() {

    private val weatherApi: WeatherApi =
        WeatherApi.getRetrofit()
    private val binder = LocalBinder()
    private var disposableTemperatureService: Disposable? = null

    private var serviceCallbacks: ServiceCallbacks? = null

    override fun onBind(intent: Intent): IBinder? {
        this.startWeatherUpdate()
        Log.d("myTag", "onBind TemperatureService")
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposableTemperatureService is Disposable && disposableTemperatureService != null) {
            if(disposableTemperatureService?.isDisposed!!){
                disposableTemperatureService?.dispose()
            }
        }
    }


    inner class LocalBinder : Binder() {
        internal// Return this instance of LocalService so clients can call public methods
        val service: TemperatureService
            get() = this@TemperatureService
    }

    private fun startWeatherUpdate() {
        disposableTemperatureService = Observable.interval(0, 10, TimeUnit.SECONDS)
            .flatMap { weatherApi.getSingleWeatherResponse().toObservable() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                serviceCallbacks?.startWeatherUpdate(it.main.temp.toString())
            }, { error ->
                throw Exception(error.localizedMessage)
            }
            )
    }


    fun setCallbacks(callbacks: ServiceCallbacks?) {
        serviceCallbacks = callbacks
    }
}
