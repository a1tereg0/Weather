package com.example.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weather.databinding.FragmentWeatherBinding
import com.example.weather.network.models.WeatherData
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class WeatherFragment: Fragment() {

    private lateinit var binding: FragmentWeatherBinding
    private lateinit var locationClient: FusedLocationProviderClient
    val viewModel by viewModels<MainViewModel> {
        val apiService = (requireActivity().applicationContext as WeatherApplication).serviceLocator.apiService
        val repository = WeatherRepository(apiService)
        MainViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentWeatherBinding.inflate(inflater, container, false)

        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (!isLocationOn()) {
            Toast.makeText(activity, "Turn on your location", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            Dexter.withContext(activity).withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        requestDataForLocation()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread().check()
        }

//        viewModel.liveCoordinate.observe(this){}

        viewModel.liveWeatherData.observe(viewLifecycleOwner) {
            it?.let { updateUI(it)
            }
        }
        return binding.root
    }


    private fun updateUI(weatherData: WeatherData) {
        with(binding) {
            temperature.text = getString(R.string.temperature_value, weatherData.current.temp)
            feelsLikeValue.text = getString(R.string.temperature_value, weatherData.current.feelsLike)
            windValue.text = getString(R.string.wind_value,weatherData.current.windSpeed)
            humidityValue.text = getString(R.string.humidity_value, weatherData.current.humidity)
            uvValue.text = getString(R.string.uv_value, weatherData.current.uvi)
            val a = weatherData.current.weather
            when(weatherData.current.weather[0].id) {
                in 200..232 -> weatherSummaryImg.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_cloud_lightning))
                in 300..321 -> weatherSummaryImg.setImageDrawable(getDrawable(requireContext(),R.drawable.ic_cloud_drizzle))
                in 500..531 -> weatherSummaryImg.setImageDrawable(getDrawable(requireContext(),R.drawable.ic_cloud_rain))
                in 600..622 -> weatherSummaryImg.setImageDrawable(getDrawable(requireContext(),R.drawable.ic_cloud_snow))
                800 -> weatherSummaryImg.setImageDrawable(getDrawable(requireContext(),R.drawable.ic_sun))
                else -> weatherSummaryImg.setImageDrawable(getDrawable(requireContext(),R.drawable.ic_cloud))
            }
        }

    }


    @SuppressLint("MissingPermission")
    private fun requestDataForLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult : LocationResult) {
            val lastLocation: Location = locationResult.lastLocation

            Coordinate.latitude = lastLocation.latitude
            Coordinate.longitude = lastLocation.longitude
            viewModel.afterSuccessfullyFetchedLocationData(Coordinate)
        }
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(requireActivity())
            .setMessage("Turn on the permissions to access app features")
            .setPositiveButton("Go to Settings") {_, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireActivity().applicationContext.packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Log.i("Error", "Something went wrong!")
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


    private fun isLocationOn(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.refresh -> {
                requestDataForLocation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}