package com.example.weather

import androidx.lifecycle.*
import com.example.weather.network.models.WeatherData
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(
    private val repository: WeatherRepository
): ViewModel() {
    val liveWeatherData: LiveData<WeatherData> get() = repository.liveWeatherData

//    init {
//        viewModelScope.launch {
//            repository.loadWeatherData()
//        }
//    }

fun afterSuccessfullyFetchedLocationData(coordinate: Coordinate) {
    viewModelScope.launch {
        repository.loadWeatherData(coordinate)
    }
}

}

class MainViewModelFactory(private val repository: WeatherRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
           return MainViewModel(repository) as T
       }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}