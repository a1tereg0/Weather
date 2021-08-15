package com.example.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.databinding.FragmentForecastsBinding

class ForecastFragment: Fragment() {

    private val viewModel by viewModels<MainViewModel> {
        val apiService = (requireActivity().applicationContext as WeatherApplication).serviceLocator.apiService
        val repository = WeatherRepository(apiService)
        MainViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentForecastsBinding.inflate(inflater, container, false)
        val adapter = ForecastListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
        viewModel.afterSuccessfullyFetchedLocationData(Coordinate)
        viewModel.liveWeatherData.observe(viewLifecycleOwner) {
            it?.let {
                adapter.forecasts = it.daily
                adapter.notifyDataSetChanged()
            }
        }
        return binding.root
    }


}