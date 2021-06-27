package com.example.astroweather

import android.content.Context
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_weather_settings.*
import org.jetbrains.anko.doAsync
import java.math.BigDecimal
import java.util.*

class WeatherSettings : Fragment() {
    private lateinit var setCityButton: Button
    private lateinit var saveButton: Button
    private lateinit var addToFavButton: Button
    private lateinit var removeFromFavButton: Button
    private lateinit var toggleUnits: ToggleButton
    private lateinit var spinnerFav: Spinner
    private lateinit var longitudeSettings: TextView
    private lateinit var latitudeSettings: TextView
    private lateinit var cityName: EditText
    private lateinit var cityTv: TextView

    private lateinit var adapter: ArrayAdapter<String>
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
        toggleUnits = view.findViewById(R.id.toggleButton)
        spinnerFav = view.findViewById(R.id.spinner_fav_cities)
        longitudeSettings = view.findViewById(R.id.longitude_setting)
        latitudeSettings = view.findViewById(R.id.latitude_setting)
        cityName = view.findViewById(R.id.et_city_name)
        cityTv = view.findViewById(R.id.city_name_settings)

        val favoriteListSharedPref = activity?.let { SharedPreferencesData.getString(context!!, "FAVORITE_CITIES") }
        Log.i("favoriteListSharedPref", SharedPreferencesData.getString(context!!, "FAVORITE_CITIES").toString())
        if (favoriteListSharedPref != null) {
            val list = favoriteListSharedPref.split(",")
            favoriteCities = list.toMutableSet()
        }

        adapter = activity?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                favoriteCities.toMutableList()
            )
        }!!

        var currentSpinnerItem: Int = spinnerFav.selectedItemPosition
        spinnerFav.adapter = adapter
        val spinnerPosition = activity?.let { SharedPreferencesData.getInt(context!!, "SPINNER_POSITION") }
        val toggleChecked = activity?.let { SharedPreferencesData.getBoolean(context!!, "TOGGLE_POSITION") }
        if (spinnerPosition != null) {
            spinnerFav.setSelection(spinnerPosition)
        }
        spinnerFav.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (checkInternetConnection()) {
                    if (currentSpinnerItem != position) {
                        cityName.setText(spinnerFav.selectedItem.toString())
                        doAsync {
                            getCoordsFromCityName()
                        }
                        saveSharedPreferencesData()
                        cityTv.text = SharedPreferencesData.getString(context!!, "CITY_NAME")
                        Log.i(
                            "onItemSelected city: ",
                            SharedPreferencesData.getString(context!!, "CITY_NAME").toString()
                        )
                        Toast.makeText(activity, "Updated Data", Toast.LENGTH_LONG).show()
                        (activity as AstroCalculator).updateFragmentsData()
                    }
                    currentSpinnerItem = position
                } else Toast.makeText(activity, "Check your internet connection", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        toggleUnits.setOnCheckedChangeListener { _, isChecked ->
            if (checkInternetConnection()) {
                if (isChecked) {
                    activity?.let { SharedPreferencesData.saveString(it, "UNITS", "imperial") }
                } else {
                    activity?.let { SharedPreferencesData.saveString(it, "UNITS", "metric") }
                }
                activity?.let { SharedPreferencesData.saveBoolean(it, "TOGGLE_POSITION", toggleButton.isChecked) }
                (activity as AstroCalculator).updateFragmentsData()
                (activity as AstroCalculator).updateFragments()
            }
        }

        longitudeSettings.text = longitude
        latitudeSettings.text = latitude

        cityTv.text = SharedPreferencesData.getString(context!!, "CITY_NAME")


        setCityButton.setOnClickListener {
            if (checkInternetConnection()) {
                doAsync {
                    getCoordsFromCityName()
                }
                saveSharedPreferencesData()
                Log.i("city updated", SharedPreferencesData.getString(context!!, "CITY_NAME").toString())
                cityTv.text = SharedPreferencesData.getString(context!!, "CITY_NAME")
                Toast.makeText(activity, "Updated Data", Toast.LENGTH_LONG).show()
                (activity as AstroCalculator).updateFragmentsData()
            } else Toast.makeText(activity, "Check your internet connection", Toast.LENGTH_LONG).show()

        }

        saveButton.setOnClickListener {
            (activity as AstroCalculator).updateFragments()
        }

        addToFavButton.setOnClickListener {
            favoriteCities.add(cityTv.text.toString())
            updateSpinner()
            (activity as AstroCalculator).getWeatherData()
            saveSharedPreferencesData()
        }
        removeFromFavButton.setOnClickListener {
            favoriteCities.remove(cityTv.text.toString())
            updateSpinner()
            saveSharedPreferencesData()
        }
    }


    fun astroDataSetter(astroData: AstroData) {
        this.astroData = astroData
    }

    override fun onResume() {
//        setAstroDataToTV(weatherData)
        super.onResume()
    }

    private fun saveSharedPreferencesData() {
        activity?.let {
            SharedPreferencesData.saveString(it, getString(R.string.lat_key), latitudeSettings.text.toString())
            activity?.let {
                SharedPreferencesData.saveString(
                    it,
                    getString(R.string.long_key),
                    longitudeSettings.text.toString()
                )
            }
            activity?.let {
                SharedPreferencesData.saveString(
                    it,
                    "CITY_NAME", cityName.text.toString()
                )
            }
            activity?.let {
                SharedPreferencesData.saveString(
                    it,
                    "FAVORITE_CITIES",
                    java.lang.String.join(",", favoriteCities)
                )
            }
            activity?.let { SharedPreferencesData.save(it, "SPINNER_POSITION", spinnerFav.selectedItemPosition) }
            Log.i("Saved favorite cities", SharedPreferencesData.getString(context!!, "FAVORITE_CITIES").toString())
            val latitudevalue: String = getString(R.string.lat_key)
        }
    }

    private fun updateSpinner() {
        adapter.clear()
        adapter.addAll(favoriteCities)
        adapter.notifyDataSetChanged()
    }

    private fun checkInternetConnection(): Boolean {
        val check = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isActive: NetworkInfo? = check.activeNetworkInfo
        return isActive?.isConnected == true
    }

    private fun getCoordsFromCityName() {
        val geocoder = Geocoder(activity, Locale.ENGLISH)
        Log.i("City name", cityName.text.toString())
        val address = geocoder.getFromLocationName(cityName.text.toString(), 10)
        if (address != null && address.isNotEmpty())
            activity?.runOnUiThread {
                cityName.setText(address[0].locality.toString())
                var lat = BigDecimal(address[0].latitude)
                lat = lat.setScale(4, BigDecimal.ROUND_HALF_UP)

                var lon = BigDecimal(address[0].longitude)
                lon = lon.setScale(4, BigDecimal.ROUND_HALF_UP)

                latitudeSettings.text = lat.toString()
                longitudeSettings.text = lon.toString()
            }
        else
            activity?.runOnUiThread {
                Toast.makeText(activity, "Couldn't find city name from given coordinates", Toast.LENGTH_SHORT).show()
            }
    }
}