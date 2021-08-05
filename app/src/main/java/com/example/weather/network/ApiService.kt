package com.example.weather.network;

import com.example.weather.network.models.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("onecall")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String = "hourly,minutely",
        @Query("units") units: String = "metric",
        @Query("appid") appid: String
    ): WeatherData
}