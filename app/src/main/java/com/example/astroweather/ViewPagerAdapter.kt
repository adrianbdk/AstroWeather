package com.example.astroweather
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WeatherSettings()
            1 -> SunPage()
            2 -> MoonPage()
            else -> WeatherPage()
        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}