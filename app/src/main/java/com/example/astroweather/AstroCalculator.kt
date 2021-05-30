package com.example.astroweather

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class AstroCalculator : AppCompatActivity() {

    private lateinit var astroData: AstroData
    private var handler: Handler? = Handler(Looper.getMainLooper())
    private var handlerTask: Runnable? = null
    private var sunFragmentPage = SunPage()
    private var moonFragmentPage = MoonPage()
    private val fragmentsList: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astro)
        astroData = AstroData()
        this.supportActionBar?.hide()
        fragmentsList.add("Sun")
        fragmentsList.add("Moon")
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val interval = sharedPreferences.getString(
            getString(R.string.interval_key),
            getString(R.string.default_interval)
        )!!.toLong()

        if (resources.getBoolean(R.bool.isTab)) {
            sunFragmentPage = supportFragmentManager.findFragmentById(R.id.fragment_sun) as SunPage
            moonFragmentPage = supportFragmentManager.findFragmentById(R.id.fragment_moon) as MoonPage
            updateFragmentsData()
            refreshData(interval)
            Log.i("Mobile/Tablet", "TABLET")
        } else {
            Log.i("Mobile/Tablet", "MOBILE")

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

    private fun updateFragmentsData() {
        sunFragmentPage.astroDataSetter(astroData)
        moonFragmentPage.astroDataSetter(astroData)
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

    private fun updateFragments() {
        updateFragmentsData()
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        for (fragment in supportFragmentManager.fragments) {
            ft.detach(fragment)
            ft.attach(fragment)
        }
        ft.commit()
    }
}

