package com.gabrifermar.proyectodam.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gabrifermar.proyectodam.model.ProyectoDAMapp
import com.gabrifermar.proyectodam.R
import kotlinx.android.synthetic.main.item_weather.view.*

class MetarAdapter(private val metars: List<String>, context: Context) :
    RecyclerView.Adapter<MetarAdapter.MetarHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetarHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return MetarHolder(layoutInflater.inflate(R.layout.item_weather, parent, false))
    }

    override fun onBindViewHolder(holder: MetarHolder, position: Int) {
        holder.show(metars[position])

        holder.reset()

        val repository = holder.view.context.applicationContext as ProyectoDAMapp

        var selected = false

        holder.view.weather_item_star.setOnClickListener {
            selected = if (!selected) {
                it.setBackgroundResource(R.drawable.ic_baseline_star_full)
                repository.repository.addFav(holder.view.weather_item_metar.text.toString())
                true
            } else {

                it.setBackgroundResource(R.drawable.ic_baseline_star_empty)
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return metars.size
    }

    class MetarHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun show(metars: String) {
            view.weather_item_metar.text = metars
        }

        fun reset() {
            view.weather_item_star.setBackgroundResource(R.drawable.ic_baseline_star_empty)
        }
    }


}