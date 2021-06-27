package com.example.astroweather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.astroweather.json_model.Root
import com.google.gson.Gson
import kotlin.math.roundToInt

class WeatherDetailsPage : Fragment() {
    private var cityName: TextView? = null
    private var windSpeed: TextView? = null
    private var windDirection: TextView? = null
    private var cloudiness: TextView? = null
    private var humidity: TextView? = null
    private var visibility: TextView? = null

    private lateinit var astroData: AstroData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather_details, container, false)
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
        cityName = view?.findViewById(R.id.location_details) as TextView
        windSpeed = view?.findViewById(R.id.wind_speed) as TextView
        windDirection = view?.findViewById(R.id.wind_direction) as TextView
        cloudiness = view?.findViewById(R.id.cloudiness) as TextView
        humidity = view?.findViewById(R.id.humidity) as TextView
        visibility = view?.findViewById(R.id.visibility) as TextView

        val units: String = SharedPreferencesData.getString(context!!, "UNITS").toString()
        windSpeed!!.text = String.format("Speed: %d m/s", weatherData.current.wind_speed.roundToInt())
        windDirection!!.text = String.format("Direction: %s°", weatherData.current.wind_deg.toString())
        cloudiness!!.text = String.format("Cloudiness: %s", weatherData.current.clouds.toString().plus("%"))
        humidity!!.text = String.format("Humidity: %s", weatherData.current.humidity.toString().plus("%"))
        visibility?.text = weatherData.current.visibility.toString().plus("m")
        cityName!!.text = activity?.let { SharedPreferencesData.getString(context!!, "CITY_NAME") }
        Log.i("DETAILS CITY", SharedPreferencesData.getString(context!!, "CITY_NAME").toString())

    }

    private fun loadSharedPreferences(): Root {
        val weatherInfoJson = activity?.let {
            SharedPreferencesData.getString(it, "WEATHER_INFO")
        }

        val gson = Gson()
        return gson.fromJson(weatherInfoJson, Root::class.java)
    }
}