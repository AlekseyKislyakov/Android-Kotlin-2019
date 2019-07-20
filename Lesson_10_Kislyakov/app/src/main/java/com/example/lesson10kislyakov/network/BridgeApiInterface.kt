package com.example.lesson10kislyakov.network

import com.example.lesson10kislyakov.models.BridgeResponse
import com.google.gson.GsonBuilder
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface BridgeApiService {

    @GET("api/v1/bridges/?format=json")
    fun getSingleBridgeResponse(): Single<BridgeResponse>

    companion object Factory {
        val BASE_URL = "http://gdemost.handh.ru/"
        fun getRetrofit(): BridgeApiService {

            val TIME_FORMAT = "HH:mm:ss"

            val gson = GsonBuilder()
                .setDateFormat(TIME_FORMAT)
                .create()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://gdemost.handh.ru/")
                .build()

            return retrofit.create(BridgeApiService::class.java)
        }
    }
}