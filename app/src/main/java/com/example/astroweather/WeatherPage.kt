package com.example.astroweather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.astroweather.json_model.Root
import com.google.gson.Gson
import java.util.*
import kotlin.math.roundToInt

class WeatherPage : Fragment() {
    private var weatherDescription: TextView? = null
    private var temperature: TextView? = null
    private var minTemperature: TextView? = null
    private var maxTemperature: TextView? = null
    private var feelsLikeTemperature: TextView? = null
    private var cityName: TextView? = null
    private var imageWeather: ImageView? = null
    private var weatherDesc: TextView? = null
    private var airPressure: TextView? = null

    private lateinit var astroData: AstroData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weatherInfo = loadSharedPreferences()
        setAstroDataToTV(weatherInfo)

    }

    fun astroDataSetter(astroData: AstroData) {
        this.astroData = astroData
    }

    override fun onResume() {
        val weatherData = loadSharedPreferences()
        setAstroDataToTV(weatherData)
        super.onResume()
    }

    private fun setAstroDataToTV(weatherData: Root) {
        temperature = view?.findViewById(R.id.temperature) as TextView
        minTemperature = view?.findViewById(R.id.temp_min) as TextView
        maxTemperature = view?.findViewById(R.id.temp_max) as TextView
        cityName = view?.findViewById(R.id.location) as TextView
        imageWeather = view?.findViewById(R.id.weather_icon) as ImageView
        weatherDesc = view?.findViewById(R.id.weather_description) as TextView
        airPressure = view?.findViewById(R.id.air_pressure) as TextView

        val units: String = SharedPreferencesData.getString(context!!, "UNITS").toString()
        if(units == "imperial") {
            temperature!!.text = String.format("%d°F", weatherData.current.temp.roundToInt())
            maxTemperature!!.text = String.format("Max temp: %d°F", weatherData.daily[0].temp.max.roundToInt())
            minTemperature!!.text = String.format("Min temp: %d°F", weatherData.daily[0].temp.min.roundToInt())
        }else if(units == "metric") {
            temperature!!.text = String.format("%d°C", weatherData.current.temp.roundToInt())
            maxTemperature!!.text = String.format("Max temp: %d°C", weatherData.daily[0].temp.max.roundToInt())
            minTemperature!!.text = String.format("Min temp: %d°C", weatherData.daily[0].temp.min.roundToInt())
        }

        weatherDesc!!.text = weatherData.current.weather[0].description.capitalize(Locale.ENGLISH)
        airPressure!!.text = String.format("Air pressure: %d hPa", weatherData.current.pressure)
        cityName!!.text = activity?.let { SharedPreferencesData.getString(context!!, "CITY_NAME") }
        when(weatherData.current.weather[0].icon){
            "01d" -> imageWeather?.setImageResource(R.drawable.ic_01d)
            "01n" -> imageWeather?.setImageResource(R.drawable.ic_01n)
            "02d" -> imageWeather?.setImageResource(R.drawable.ic_02d)
            "02n" -> imageWeather?.setImageResource(R.drawable.ic_02n)
            "03d" -> imageWeather?.setImageResource(R.drawable.ic_03d)
            "03n" -> imageWeather?.setImageResource(R.drawable.ic_03n)
            "04d" -> imageWeather?.setImageResource(R.drawable.ic_04d)
            "04n" -> imageWeather?.setImageResource(R.drawable.ic_04n)
            "09d" -> imageWeather?.setImageResource(R.drawable.ic_09d)
            "09n" -> imageWeather?.setImageResource(R.drawable.ic_09n)
            "10d" -> imageWeather?.setImageResource(R.drawable.ic_10d)
            "10n" -> imageWeather?.setImageResource(R.drawable.ic_10n)
            "11d" -> imageWeather?.setImageResource(R.drawable.ic_11d)
            "11n" -> imageWeather?.setImageResource(R.drawable.ic_11n)
            "13d" -> imageWeather?.setImageResource(R.drawable.ic_13d)
            "13n" -> imageWeather?.setImageResource(R.drawable.ic_13n)
            "50d" -> imageWeather?.setImageResource(R.drawable.ic_50d)
            "50n" -> imageWeather?.setImageResource(R.drawable.ic_50n)
        }
        Log.i("WeatherPage city", SharedPreferencesData.getString(context!!, "CITY_NAME").toString())
    }

    private fun loadSharedPreferences(): Root {
        val weatherInfoJson = activity?.let {
            SharedPreferencesData.getString(it,"WEATHER_INFO")
        }

        val gson = Gson()
        return gson.fromJson(weatherInfoJson, Root::class.java)
    }
}