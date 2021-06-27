package com.example.astroweather
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.astrocalculator.AstroCalculator

class SunPage : Fragment() {

    private lateinit var astroData: AstroData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sun, container, false)
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

    private fun setAstroDataToTV(astroCalculator: AstroCalculator){
        val latitude = SharedPreferencesData.getString(context!!, getString(R.string.lat_key)).toString()
        val longitude = SharedPreferencesData.getString(context!!, getString(R.string.long_key)).toString()

        view?.findViewById<TextView>(R.id.sunrise)?.text =
            astroData.getTimeToSting(astroCalculator.sunInfo.sunrise)
        view?.findViewById<TextView>(R.id.sunset)?.text =
            astroData.getTimeToSting(astroCalculator.sunInfo.sunset)
        view?.findViewById<TextView>(R.id.civil_dawn)?.text =
            astroData.getTimeToSting(astroCalculator.sunInfo.twilightMorning)
        view?.findViewById<TextView>(R.id.civil_twilight)?.text =
            astroData.getTimeToSting(astroCalculator.sunInfo.twilightEvening)

        view?.findViewById<TextView>(R.id.sunrise_azimuth)?.text =
            astroData.getAzimuthToString(astroCalculator.sunInfo.azimuthRise)
        view?.findViewById<TextView>(R.id.sunset_azimuth)?.text =
            astroData.getAzimuthToString(astroCalculator.sunInfo.azimuthSet)

        view?.findViewById<TextView>(R.id.latitude)?.text =
            latitude?.let { astroData.getLatitudeToString(it) }
        view?.findViewById<TextView>(R.id.longitude)?.text =
            longitude?.let { astroData.getLongitudeToString(it) }

        view?.findViewById<TextView>(R.id.current_date)?.text =
            astroData.getDateToString(astroCalculator.dateTime)
    }
}