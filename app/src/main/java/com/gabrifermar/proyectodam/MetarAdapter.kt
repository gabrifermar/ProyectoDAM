package com.gabrifermar.proyectodam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_weather.view.*

class MetarAdapter(private val metars: List<String>) : RecyclerView.Adapter<MetarAdapter.MetarHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetarHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MetarHolder(layoutInflater.inflate(R.layout.item_weather, parent, false))
    }

    override fun onBindViewHolder(holder: MetarHolder, position: Int) {
        holder.show(metars[position])
    }

    override fun getItemCount(): Int {
        return metars.size
    }

    class MetarHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun show(metars: String) {
            view.weather_item_metar.text = metars
        }
    }


}