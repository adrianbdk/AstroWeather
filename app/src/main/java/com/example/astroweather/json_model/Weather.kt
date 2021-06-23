package com.example.astroweather.json_model

data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)