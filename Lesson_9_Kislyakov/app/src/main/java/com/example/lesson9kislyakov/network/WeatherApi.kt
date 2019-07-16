package com.example.lesson9kislyakov.network

import com.example.lesson9kislyakov.models.WeatherData
import com.google.gson.GsonBuilder
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface WeatherApi {

    @GET("weather?q=saransk&units=metric&appid=a924f0f5b30839d1ecb95f0a6416a0c2")
    fun getSingleWeatherResponse(): Single<WeatherData>

    companion object Factory {
        private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
        fun getRetrofit(): WeatherApi {
            val gson = GsonBuilder()
                .create()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(WeatherApi::class.java)
        }
    }
}