package com.example.weather

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = Tab.values().size
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> WeatherFragment()
            1 -> ForecastFragment()
            else -> throw Error("")
        }
    }
}

enum class Tab(@StringRes val res: Int) {
    CURRENT(R.string.current_weather),
    DAILY(R.string.daily_weather)
}