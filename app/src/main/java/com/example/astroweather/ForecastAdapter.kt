package com.example.astroweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.astroweather.json_model.Root
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class ForecastAdapter(private val weatherData: Root, private val units: String) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val forecastDescription: TextView = view.findViewById(R.id.tv_weather_desc)
        val forecastDate: TextView = view.findViewById(R.id.tv_date)
        val forecastIcon: ImageView = view.findViewById(R.id.weather_image)
        val forecastTemperature: TextView = view.findViewById(R.id.forecast_temperature)
        init{

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.forecast_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        when (weatherData.daily[position].weather[0].icon) {
            "01d" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_01d)
            "01n" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_01n)
            "02d" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_02d)
            "02n" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_02n)
            "03d" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_03d)
            "03n" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_03n)
            "04d" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_04d)
            "04n" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_04n)
            "09d" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_09d)
            "09n" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_09n)
            "10d" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_10d)
            "10n" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_10n)
            "11d" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_11d)
            "11n" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_11n)
            "13d" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_13d)
            "13n" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_13n)
            "50d" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_50d)
            "50n" -> viewHolder.forecastIcon.setImageResource(R.drawable.ic_50n)
        }
        viewHolder.forecastDescription.text = weatherData.daily[position].weather[0].description.capitalize(Locale.ENGLISH)
        val dateFormatter = SimpleDateFormat("EEEE, d MMMM", Locale.ENGLISH)
        val dateToString = dateFormatter.format(Date(weatherData.daily[position].dt * 1000)).toString()
        viewHolder.forecastDate.text = dateToString
        viewHolder.forecastTemperature.text = String.format("%d$units", weatherData.daily[position].temp.day.roundToInt())
    }

    override fun getItemCount() = weatherData.daily.size

}