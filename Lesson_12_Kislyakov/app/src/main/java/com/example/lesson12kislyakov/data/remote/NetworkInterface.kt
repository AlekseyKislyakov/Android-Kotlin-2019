package com.example.lesson12kislyakov.data.remote

import android.content.Context
import com.example.lesson12kislyakov.data.models.Bridge
import com.example.lesson12kislyakov.data.models.BridgeResponse
import com.google.gson.GsonBuilder
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkService {

    @GET("api/v1/bridges/{id}/?format=json")
    fun getOneSingleBridge(@Path("id") bridgeId: Int): Single<Bridge>

    @GET("api/v1/bridges/?format=json")
    fun getSingleBridgeResponse(): Single<BridgeResponse>

    companion object Factory {
        val BASE_URL = "http://gdemost.handh.ru/"
        fun getRetrofit(): NetworkService {

            val TIME_FORMAT = "HH:mm:ss"

            val gson = GsonBuilder()
                .setDateFormat(TIME_FORMAT)
                .create()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://gdemost.handh.ru/")
                .build()

            return retrofit.create(NetworkService::class.java)
        }
    }
}