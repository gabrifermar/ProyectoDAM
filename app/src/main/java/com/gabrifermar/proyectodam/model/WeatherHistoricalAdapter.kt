package com.gabrifermar.proyectodam.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gabrifermar.proyectodam.R
import kotlinx.android.synthetic.main.item_weather_historical.view.*

class WeatherHistoricalAdapter(private val historical: List<String>, private val date: List<String>) :
    RecyclerView.Adapter<WeatherHistoricalAdapter.WeatherHistoricalHolder>() {

    class WeatherHistoricalHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun show(date: String, data: String) {
            view.weather_item_historical_date.text = date
            view.weather_item_historical_metar.text = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHistoricalHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return WeatherHistoricalHolder(
            layoutInflater.inflate(
                R.layout.item_weather_historical,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherHistoricalHolder, position: Int) {
        holder.show(date[position], historical[position])
    }

    override fun getItemCount(): Int {
        return historical.size
    }
}