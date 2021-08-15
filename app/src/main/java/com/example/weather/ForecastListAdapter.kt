package com.example.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.ViewForecastItemBinding
import com.example.weather.network.models.ForecastData

class ForecastListAdapter: RecyclerView.Adapter<ForecastListViewHolder>() {
    var forecasts: List<ForecastData> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastListViewHolder {
        return ForecastListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ForecastListViewHolder, position: Int) {
        holder.bind(forecasts[position])
    }

    override fun getItemCount(): Int = forecasts.size
}

class ForecastListViewHolder private constructor(private val binding: ViewForecastItemBinding): RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent:ViewGroup):ForecastListViewHolder {
            val binding = ViewForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ForecastListViewHolder(binding)
        }
    }

    fun bind(forecast: ForecastData) {
        updateUI(forecast)
    }

    fun updateUI(forecast: ForecastData) {
        binding.humidityTextView.text = "Humidity: "+forecast.humidity.toString()+" %"
        binding.temperatureTextView.text = "Max Temp: "+forecast.temp.max.toString()+" °C"
        binding.windTextView.text = "Wind Speed: "+forecast.windSpeed.toString()+"metre/sec"
    }

}