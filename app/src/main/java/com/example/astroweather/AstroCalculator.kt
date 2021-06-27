package com.example.astroweather

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.example.astroweather.API.APIEndpoints
import com.example.astroweather.API.RetrofitBuilder
import com.example.astroweather.json_model.Root
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AstroCalculator : AppCompatActivity() {
    private lateinit var astroData: AstroData
    private var handler: Handler? = Handler(Looper.getMainLooper())
    private var handlerTask: Runnable? = null
    private var sunFragmentPage = SunPage()
    private var moonFragmentPage = MoonPage()
    private var weatherFragmentPage = WeatherPage()
    private val fragmentsList: MutableList<String> = ArrayList()
    private var units: String = "metric"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astro)
        astroData = AstroData()
        this.supportActionBar?.hide()
        fragmentsList.add("Settings")
        fragmentsList.add("Sun")
        fragmentsList.add("Moon")
        fragmentsList.add("Weather")
        fragmentsList.add("Details")
        fragmentsList.add("Forecast")
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val interval = sharedPreferences.getString(
            getString(R.string.interval_key),
            getString(R.string.default_interval)
        )!!.toLong()


            val tabLayout = findViewById<TabLayout>(R.id.tabs)
            val viewPager2 = findViewById<ViewPager2>(R.id.view_pager)
            val adapter = ViewPagerAdapter(this)
            viewPager2.adapter = adapter
            TabLayoutMediator(
                tabLayout, viewPager2
            ) { tab, position -> tab.text = fragmentsList[position] }.attach()
            updateFragmentsData()
            refreshData(interval)

    }

    override fun onStop() {
        super.onStop()
        handler?.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        updateFragmentsData()
    }

    override fun onPause() {
        super.onPause()
        clearHandler()
    }

    fun updateFragmentsData() {
        sunFragmentPage.astroDataSetter(astroData)
        moonFragmentPage.astroDataSetter(astroData)
        getWeatherData()
        units = SharedPreferencesData.getString(this, "UNITS").toString()
        Log.i("UNITS", SharedPreferencesData.getString(this, "UNITS").toString())
    }

    private fun refreshData(seconds: Long) {
        clearHandler()
        handlerTask = Runnable {
            Log.i("isRefreshing", "refresh")
            updateFragments()

            handler!!.postDelayed(handlerTask!!, seconds * 1000)
        }
        handlerTask!!.run()
    }

    private fun clearHandler() {
        handlerTask?.let { handler!!.removeCallbacksAndMessages(it) }
    }

    fun updateFragments() {
        updateFragmentsData()
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        for (fragment in supportFragmentManager.fragments) {
            ft.detach(fragment)
            ft.attach(fragment)
        }
        ft.commit()
    }
    fun getWeatherData(){
        val latitude: String = SharedPreferencesData.getString(this, getString(R.string.lat_key)).toString()
        val longitude: String = SharedPreferencesData.getString(this, getString(R.string.long_key)).toString()

        val service = RetrofitBuilder.create(APIEndpoints::class.java)
        val call = service.getWeather(
            latitude,
            longitude,
            "hour,minutely,alerts",
            units,
            getString(R.string.openWeatherMapAPI))
        call.enqueue(object: Callback<Root> {
            override fun onResponse(call: Call<Root>?, response: Response<Root>?) {
                val gson = Gson()
                val jsonToString = gson.toJson(response?.body())
                Log.i("jsonFromJsonfromgson", jsonToString)
                SharedPreferencesData.saveString(application, "WEATHER_INFO", jsonToString)
            }

            override fun onFailure(call: Call<Root>?, t: Throwable?) {
                Toast.makeText(this@AstroCalculator, "${t?.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}

