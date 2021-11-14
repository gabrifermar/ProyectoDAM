package com.gabrifermar.proyectodam

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_flight_planner.view.*

class FlightPlannerAdapter(
    private val context: Context,
    private val waypoints: List<String>,
    private val distances: List<Double>,
    private val headings: List<Int>
) : RecyclerView.Adapter<FlightPlannerAdapter.FlightPlannerHolder>() {

    class FlightPlannerHolder(val view: View) :
        RecyclerView.ViewHolder(view) {

        fun show(context: Context, waypoint: String, distance: Double, hdg: Int) {
            view.item_flight_planner_name.text = waypoint
            view.item_flight_planner_distance.text = context.getString(R.string.distance, distance)
            view.item_flight_planner_hdg.text =
                context.getString(R.string.heading, hdg.toString().padStart(3, '0'))
            view.item_flight_planner_distance.visibility = View.VISIBLE
            view.item_flight_planner_hdg.visibility = View.VISIBLE
            view.view1.visibility = View.VISIBLE
            view.view2.visibility = View.VISIBLE
        }

        fun showEnd(waypoint: String) {
            view.item_flight_planner_name.text = waypoint
            view.item_flight_planner_distance.visibility = View.INVISIBLE
            view.item_flight_planner_hdg.visibility = View.INVISIBLE
            view.view1.visibility = View.INVISIBLE
            view.view2.visibility = View.INVISIBLE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightPlannerHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FlightPlannerHolder(
            layoutInflater.inflate(
                R.layout.item_flight_planner,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FlightPlannerHolder, position: Int) {
        //null treatment
        if (distances[position] == 0.0 || headings[position] == 0) {
            holder.showEnd(waypoints[position])
        } else {
            holder.show(context, waypoints[position], distances[position], headings[position])
        }


    }

    override fun getItemCount(): Int {
        return waypoints.size
    }
}