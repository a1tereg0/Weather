package com.example.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.network.ApiService
import com.example.weather.network.models.WeatherData

class WeatherRepository(private val apiService: ApiService) {
    private val _liveWeatherData = MutableLiveData<WeatherData>()
    val liveWeatherData: LiveData<WeatherData> get() = _liveWeatherData

    suspend fun loadWeatherData(coordinate: Coordinate) {
        val result = try {
            apiService.getWeatherData(coordinate.latitude, coordinate.longitude, appid = BundleKeys.API_KEY)
        } catch (e: Exception){
            null
        }
        result?.let {
            _liveWeatherData.value = it
        }
    }
}