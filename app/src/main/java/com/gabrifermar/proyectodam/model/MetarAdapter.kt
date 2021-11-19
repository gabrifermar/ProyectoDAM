package com.gabrifermar.proyectodam.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gabrifermar.proyectodam.R
import kotlinx.android.synthetic.main.item_weather.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MetarAdapter(private val metars: List<String>, context: Context) :
    RecyclerView.Adapter<MetarAdapter.MetarHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetarHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return MetarHolder(layoutInflater.inflate(R.layout.item_weather, parent, false))
    }

    override fun onBindViewHolder(holder: MetarHolder, position: Int) {
        holder.show(metars[position])

        var selected = false


        holder.view.weather_item_star.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                selected = if (!selected) {
                    it.setBackgroundResource(R.drawable.ic_baseline_star_full)
                    true
                } else {
                    it.setBackgroundResource(R.drawable.ic_baseline_star_empty)
                    false
                }
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
    }


}