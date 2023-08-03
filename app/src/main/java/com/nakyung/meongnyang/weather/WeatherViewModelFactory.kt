package com.nakyung.meongnyang.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WeatherViewModelFactory(private val la: Double, private val lo: Double, private val district: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(la, lo, district) as T
        }
        throw IllegalAccessException("Un")
    }
}