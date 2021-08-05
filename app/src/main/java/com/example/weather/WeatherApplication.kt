package com.example.weather

import android.app.Application

class WeatherApplication: Application() {
    val serviceLocator = ServiceLocator()
}