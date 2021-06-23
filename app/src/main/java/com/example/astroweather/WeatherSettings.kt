package com.example.astroweather

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import java.math.BigDecimal
import java.util.*

class WeatherSettings : Fragment() {
    private lateinit var setCityButton: Button
    private lateinit var saveButton: Button
    private lateinit var addToFavButton: Button
    private lateinit var removeFromFavButton: Button
    private lateinit var toggleButton: ToggleButton
    private lateinit var spinnerFavCities: Spinner
    private lateinit var longitudeSettings: TextView
    private lateinit var latitudeSettings: TextView
    private lateinit var cityName: EditText

    private lateinit var adapter : ArrayAdapter<String>
    private var favoriteCities = mutableSetOf("Lodz")

    private lateinit var astroData: AstroData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val weatherInfo = loadSharedPreferences()
//        setAstroDataToTV(weatherInfo)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        var latitude = sharedPreferences.getString(
            getString(R.string.lat_key),
            getString(R.string.latitude)
        )
        var longitude = sharedPreferences.getString(
            getString(R.string.long_key),
            getString(R.string.longitude)
        )
        astroData = AstroData()
        if (longitude != null && latitude != null) {
            astroData.updateLocation(latitude.toDouble(), longitude.toDouble())
            Log.i("Latitude", "${latitude.toDouble()}")
            Log.i("Longitude", "${longitude.toDouble()}")
        }

        saveButton = view.findViewById(R.id.button_refresh)
        addToFavButton = view.findViewById(R.id.button_add_to_fav)
        removeFromFavButton = view.findViewById(R.id.button_remove_from_fav)
        setCityButton = view.findViewById(R.id.button_set_city)
        toggleButton = view.findViewById(R.id.toggleButton)
        spinnerFavCities = view.findViewById(R.id.spinner_fav_cities)
        longitudeSettings = view.findViewById(R.id.longitude_setting)
        latitudeSettings = view.findViewById(R.id.latitude_setting)
        cityName = view.findViewById(R.id.et_city_name)

        adapter = activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                favoriteCities.toMutableList()
            )
        }!!
        spinnerFavCities.adapter = adapter

        longitudeSettings.text = longitude
        latitudeSettings.text = latitude

        setCityButton.setOnClickListener {
            val geocoder = Geocoder(activity, Locale.ENGLISH)
            Log.i("City name", cityName.text.toString())
            val address = geocoder.getFromLocationName(cityName.text.toString(), 10)

            var lat = BigDecimal(address[0].latitude)
            lat = lat.setScale(4, BigDecimal.ROUND_HALF_UP)


            var lon = BigDecimal(address[0].longitude)
            lon = lon.setScale(4, BigDecimal.ROUND_HALF_UP)

            latitudeSettings.text = lat.toString()
            longitudeSettings.text = lon.toString()

            saveSharedPreferencesData()
            Log.i("SharedPreferences pobieranie danych", SharedPreferencesData.getString(context!!, getString(R.string.lat_key)).toString())
            Toast.makeText(activity, "Updated Data", Toast.LENGTH_LONG).show()
            (activity as AstroCalculator).updateFragmentsData()
        }

        saveButton.setOnClickListener {
            (activity as AstroCalculator).updateFragments()
        }
    }


    fun astroDataSetter(astroData: AstroData) {
        this.astroData = astroData
    }

    override fun onResume() {
//        setAstroDataToTV(weatherData)
        super.onResume()
    }


//    private fun setAstroDataToTV(weatherData: Root) {
//        view?.findViewById<Button>(R.id.button_remove_from_fav)?.text =
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
//    }

//    private fun loadSharedPreferences(): Root {
//        val weatherInfoJson = activity?.let {
//            context?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
//                ?.getString("WEATHER_DATA", null)
//        }
//        cityName?.text = activity?.let {
//            context?.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
//                ?.getString("CITY_NAME", null)
//        }
//        val gson = Gson()
//        return gson.fromJson(weatherInfoJson, Root::class.java)
//    }

    private fun saveSharedPreferencesData() {
        activity?.let { SharedPreferencesData.saveString(it, getString(R.string.lat_key), latitudeSettings.text.toString())
            Log.i("SharedPreferences wysyłanie danych", latitudeSettings.text.toString())}
        activity?.let { SharedPreferencesData.saveString(it, getString(R.string.long_key), longitudeSettings.text.toString())
            Log.i("SharedPreferences wysyłanie danych", longitudeSettings.text.toString())}
        activity?.let { SharedPreferencesData.saveString(it, "CITY_NAME", cityName.text.toString()) }
        activity?.let { SharedPreferencesData.saveString(it, "FAVORITE_CITIES",java.lang.String.join(",", favoriteCities)) }
        val latitudevalue: String = getString(R.string.lat_key)

    }
}