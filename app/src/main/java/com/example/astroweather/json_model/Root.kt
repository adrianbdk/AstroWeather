package com.example.astroweather.json_model

data class Root(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Long,
    val current: Current,
    val daily: List<Daily>
)