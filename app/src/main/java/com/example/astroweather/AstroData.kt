package com.example.astroweather

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.icu.util.Calendar
import com.astrocalculator.AstroCalculator
import com.astrocalculator.AstroDateTime


class AstroData {
    var astroCalculator: AstroCalculator

    private fun setDate(): AstroDateTime {
        val currentDate = Calendar.getInstance()
        val day = currentDate[Calendar.DAY_OF_MONTH]
        val month = currentDate[Calendar.MONTH] + 1
        val year = currentDate[Calendar.YEAR]
        val hour = currentDate[Calendar.HOUR_OF_DAY]
        val minute = currentDate[Calendar.MINUTE]
        val second = currentDate[Calendar.SECOND]
        return AstroDateTime(year, month, day, hour, minute, second, 1, true)
    }

    private fun setLocation(latitude: Double, longitude: Double): AstroCalculator.Location {
        return AstroCalculator.Location(latitude, longitude)
    }

    fun updateLocation(latitude: Double, longitude: Double): AstroCalculator {
        astroCalculator = AstroCalculator(setDate(), setLocation(latitude, longitude))
        return astroCalculator
    }

    fun getTimeToSting(astroDateTime: AstroDateTime): String {
        return String.format("%02d:%02d", astroDateTime.hour, astroDateTime.minute)
    }

    fun getDateToString(astroDateTime: AstroDateTime): String {
        val months: HashMap<Int, String> = HashMap()
        months[1] = "Jan"
        months[2] = "Feb"
        months[3] = "Mar"
        months[4] = "Apr"
        months[5] = "May"
        months[6] = "Jun"
        months[7] = "Jul"
        months[8] = "Aug"
        months[9] = "Sept"
        months[10] = "Oct"
        months[11] = "Nov"
        months[12] = "Dec"

        return ("${astroDateTime.day} ${months[astroDateTime.month]} ${astroDateTime.year}, ")
    }

    fun getAzimuthToString(azimuth: Double): String {
        val formatter: NumberFormat = DecimalFormat("#0.00000")
        return formatter.format(azimuth)
    }

    fun getLatitudeToString(latitude: String): String {
        return (latitude)
    }

    fun getLongitudeToString(longitude: String): String {
        return (longitude)
    }

    init {
        astroCalculator = AstroCalculator(setDate(), setLocation(0.0, 0.0))
    }
}