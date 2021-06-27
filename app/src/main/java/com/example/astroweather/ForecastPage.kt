package com.example.astroweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.astroweather.json_model.Root
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_forecast.*


class ForecastPage : Fragment() {
    private var forecastCity: TextView? = null
    private lateinit var weatherData: Root
    private var units: String? = "°C"
    private lateinit var astroData: AstroData
    private var headerTitle: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherData = loadSharedPreferences()
        updateRecycleView()
        recycler_view_forecast.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = units?.let {ForecastAdapter(weatherData, it)}
            }
        setAstroDataToTV()
    }

    private fun updateRecycleView(){
        loadSharedPreferences()
        recycler_view_forecast?.apply{
            adapter?.notifyDataSetChanged()
        }
    }
    fun astroDataSetter(astroData: AstroData) {
        this.astroData = astroData
    }

    override fun onResume() {
        val weatherData = loadSharedPreferences()
        setAstroDataToTV()
        super.onResume()
    }

    private fun setAstroDataToTV() {
        headerTitle = view?.findViewById(R.id.header_title) as TextView
        headerTitle!!.text =
            String.format("Forecast for %s",
                SharedPreferencesData.getString(context!!, "CITY_NAME")
                    .toString())

    }

    private fun loadSharedPreferences(): Root {
        val weatherInfoJson = activity?.let {
            SharedPreferencesData.getString(it,"WEATHER_INFO")
        }
        units = activity?.let{SharedPreferencesData.getString(it, "UNITS")}
        units = if(units == "metric") "°C" else "°F"

        val gson = Gson()
        return gson.fromJson(weatherInfoJson, Root::class.java)
    }
}