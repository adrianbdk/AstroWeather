package com.example.astroweather
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.astrocalculator.AstroCalculator
import java.math.BigDecimal
import java.math.RoundingMode

class MoonPage : Fragment() {

    private lateinit var astroData: AstroData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_moon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val latitude = sharedPreferences.getString(
            getString(R.string.lat_key),
            getString(R.string.latitude)
        )
        val longitude = sharedPreferences.getString(
            getString(R.string.long_key),
            getString(R.string.longitude)
        )
        astroData = AstroData()
        if (longitude != null && latitude != null) {
            astroData.updateLocation(latitude.toDouble(), longitude.toDouble())
            Log.i("Latitude", "${latitude.toDouble()}")
            Log.i("Longitude", "${longitude.toDouble()}")
        }
        setAstroDataToTV(astroData.astroCalculator)
    }

    fun astroDataSetter(astroData: AstroData){
        this.astroData = astroData
    }

    override fun onResume() {
        setAstroDataToTV(astroData.astroCalculator)
        super.onResume()
    }

    @SuppressLint("SetTextI18n")
    private fun setAstroDataToTV(astroCalculator: AstroCalculator){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val latitude = sharedPreferences.getString(
            getString(R.string.lat_key),
            getString(R.string.latitude)
        )
        val longitude = sharedPreferences.getString(
            getString(R.string.long_key),
            getString(R.string.longitude)
        )

        view?.findViewById<TextView>(R.id.moonrise)?.text =
            astroData.getTimeToSting(astroCalculator.moonInfo.moonrise)
        view?.findViewById<TextView>(R.id.moonset)?.text =
            astroData.getTimeToSting(astroCalculator.moonInfo.moonset)
        view?.findViewById<TextView>(R.id.new_moon)?.text =
            astroData.getDateToString(astroCalculator.moonInfo.nextNewMoon)
        view?.findViewById<TextView>(R.id.full_moon)?.text =
            astroData.getDateToString(astroCalculator.moonInfo.nextFullMoon)

        val phaseDecimalValue =
            BigDecimal(astroCalculator.moonInfo.illumination * 100).setScale(2, RoundingMode.HALF_EVEN)

        view?.findViewById<TextView>(R.id.phase)?.text =
            phaseDecimalValue.toString().plus("%")
        view?.findViewById<TextView>(R.id.sunset_azimuth)?.text =
            astroData.getAzimuthToString(astroCalculator.sunInfo.azimuthSet)

        view?.findViewById<TextView>(R.id.latitude)?.text =
            latitude?.let { astroData.getLatitudeToString(it) }
        view?.findViewById<TextView>(R.id.longitude)?.text =
            longitude?.let { astroData.getLongitudeToString(it) }

        val moonAge =
            BigDecimal(astroCalculator.moonInfo.age).setScale(0, RoundingMode.HALF_EVEN)
        view?.findViewById<TextView>(R.id.day)?.text =
            moonAge.toString()

        view?.findViewById<TextView>(R.id.current_date)?.text =
            astroData.getDateToString(astroCalculator.dateTime)
    }
}