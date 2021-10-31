package com.gabrifermar.proyectodam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_flight_planner.view.*

class FlightPlannerAdapter (private val waypoints:List<String>,private val distances:MutableList<Double>,private val headings:MutableList<Int>):RecyclerView.Adapter<FlightPlannerAdapter.FlightPlannerHolder>(){

    class FlightPlannerHolder(val view:View):RecyclerView.ViewHolder(view) {
        fun show(waypoint:String,distance:Double,hdg:Int){
            view.item_flight_planner_name.text=waypoint
            view.item_flight_planner_distance.text=distance.toString()
            view.item_flight_planner_hdg.text=hdg.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightPlannerHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        return FlightPlannerHolder(layoutInflater.inflate(R.layout.item_flight_planner,parent,false))
    }

    override fun onBindViewHolder(holder: FlightPlannerHolder, position: Int) {
        holder.show(waypoints[position],distances[position],headings[position])
    }

    override fun getItemCount(): Int {
        return waypoints.size
    }
}