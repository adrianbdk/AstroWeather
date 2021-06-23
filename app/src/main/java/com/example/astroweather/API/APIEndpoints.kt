package com.example.astroweather.API



import com.example.astroweather.json_model.Root
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIEndpoints {

    @GET("data/2.5/onecall")
    fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("exclude") exclude: String,
        @Query("units") units: String,
        @Query("appid") appid: String

    ): Call<Root>

}