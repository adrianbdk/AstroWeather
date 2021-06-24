package com.example.astroweather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.astroweather.json_model.Root
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.roundToInt

class WeatherPage : Fragment() {
    private var weatherDescription: TextView? = null
    private var temperature: TextView? = null
    private var minTemperature: TextView? = null
    private var maxTemperature: TextView? = null
    private var feelsLikeTemperature: TextView? = null
    private var cityName: TextView? = null

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
//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
//        val latitude = sharedPreferences.getString(
//            getString(R.string.lat_key),
//            getString(R.string.latitude)
//        )
//        val longitude = sharedPreferences.getString(
//            getString(R.string.long_key),
//            getString(R.string.longitude)
//        )
//        astroData = AstroData()
//        if (longitude != null && latitude != null) {
//            astroData.updateLocation(latitude.toDouble(), longitude.toDouble())
//            Log.i("Latitude", "${latitude.toDouble()}")
//            Log.i("Longitude", "${longitude.toDouble()}")
//        }

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

        temperature!!.text = String.format("%d°C", weatherData.current.temp.roundToInt())
        maxTemperature!!.text = String.format("Max temp: %d°C", weatherData.daily[0].temp.max.roundToInt())
        minTemperature!!.text = String.format("Min temp: %d°C", weatherData.daily[0].temp.min.roundToInt())
        cityName!!.text = activity?.let { SharedPreferencesData.getString(it, "CITY") + " weather" }


//        view?.findViewById<TextView>(R.id.sunrise)?.text =
//            astroData.getTimeToSting(astroCalculator.sunInfo.sunrise)
//        view?.findViewById<TextView>(R.id.sunset)?.text =
//            astroData.getTimeToSting(astroCalculator.sunInfo.sunset)
//        view?.findViewById<TextView>(R.id.civil_dawn)?.text =
//            astroData.getTimeToSting(astroCalculator.sunInfo.twilightMorning)
//        view?.findViewById<TextView>(R.id.civil_twilight)?.text =
//            astroData.getTimeToSting(astroCalculator.sunInfo.twilightEvening)
//
//        view?.findViewById<TextView>(R.id.sunrise_azimuth)?.text =
//            astroData.getAzimuthToString(astroCalculator.sunInfo.azimuthRise)
//        view?.findViewById<TextView>(R.id.sunset_azimuth)?.text =
//            astroData.getAzimuthToString(astroCalculator.sunInfo.azimuthSet)
//
//        view?.findViewById<TextView>(R.id.latitude)?.text =
//            latitude?.let { astroData.getLatitudeToString(it) }
//        view?.findViewById<TextView>(R.id.longitude)?.text =
//            longitude?.let { astroData.getLongitudeToString(it) }
//
//        view?.findViewById<TextView>(R.id.current_date)?.text =
//            astroData.getDateToString(astroCalculator.dateTime)
    }

    private fun getWeatherData() {
        val API_KEY: String = getString(R.string.openWeatherMapAPI)
        val CITY: String = getString(R.string.city)
        val URL = "https://api.openweathermap.org/data/2.5/weather?q=$CITY&appid=$API_KEY&units=metric"
        val queue = Volley.newRequestQueue(context);
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, URL, null,
            { response ->
                try {
                    val jsonObjMain: JSONObject = response.getJSONObject("main")
                    val jsonObjSys: JSONObject = response.getJSONObject("sys")
                    val city: String = response.getString("name")
                    val temperature: Int = jsonObjMain.getInt("temp")
                    val temperatureMin: Int = jsonObjMain.getInt("temp_min")
                    val temperatureMax: Int = jsonObjMain.getInt("temp_max")
                    val country: String = jsonObjSys.getString("country")

                    val temperatureMinString = "Min temp $temperatureMin°C"
                    val temperatureMaxString = "max temp $temperatureMax°C"
                    view?.findViewById<TextView>(R.id.temperature)?.text =
                        temperature.toString().plus("°C")
                    view?.findViewById<TextView>(R.id.temp_min)?.text =
                        temperatureMinString
                    view?.findViewById<TextView>(R.id.temp_max)?.text =
                        temperatureMaxString
                    view?.findViewById<TextView>(R.id.location)?.text =
                        city.plus(", ").plus(country)
                    Log.i("Api message", temperature.toString().plus("°C"))
                } catch (e: JSONException) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonObjectRequest)
    }

    private fun loadSharedPreferences(): Root {
        val weatherInfoJson = activity?.let {
            SharedPreferencesData.getString(it,"WEATHER_INFO")
        }
        cityName?.text = activity?.let {
            SharedPreferencesData.getString(it,"CITY_NAME")
        }
        val gson = Gson()
        return gson.fromJson(weatherInfoJson, Root::class.java)
    }
}