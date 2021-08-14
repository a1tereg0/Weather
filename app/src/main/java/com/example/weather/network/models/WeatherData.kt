package com.example.weather.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&exclude=hourly,minutely&units=metric&appid=760401b460fe4c0d8ab75c5e3117e265
@JsonClass(generateAdapter = true)
data class WeatherData (
        val current: CurrentWeather,
        val daily: List<ForecastData>
)

@JsonClass(generateAdapter = true)
data class CurrentWeather (
        val temp: Double,
        @Json(name = "feels_like")
        val feelsLike: Double,
        val pressure: Int,
        val humidity: Int,
        val visibility: Int,
        @Json(name = "wind_speed")
        val windSpeed: Double,
        @Json(name = "wind_deg")
        val windDeg: Int,
        @Json(name = "wind_gust")
        val windGust: Double,
        val weather: List<WeatherSummary>,
        val uvi: Double
        )

@JsonClass(generateAdapter = true)
data class WeatherSummary(
        val id: Int,
        val main: String,
        val description: String
)

@JsonClass(generateAdapter = true)
data class ForecastData (
        val weather: List<WeatherSummary>,
        val temp: Temperature,
        val pressure: Int,
        val humidity: Int,
        @Json(name = "wind_speed")
        val windSpeed: Double,
        @Json(name = "wind_deg")
        val windDeg: Int,
        @Json(name = "wind_gust")
        val windGust:Double
)

@JsonClass(generateAdapter = true)
data class Temperature(
        val day: Double,
        val min: Double,
        val max: Double,
        val night: Double,
        val eve: Double,
        val morn: Double
)