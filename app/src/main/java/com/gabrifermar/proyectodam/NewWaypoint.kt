package com.gabrifermar.proyectodam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.gabrifermar.proyectodam.databinding.ActivityNewWaypointBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class NewWaypoint : AppCompatActivity() {

    private lateinit var binding: ActivityNewWaypointBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewWaypointBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //variables

        binding.waypointBtnAdd.setOnClickListener {
            addwaypoint()
        }
    }

    private fun addwaypoint() {

        //variables
        val db = Firebase.firestore

        //check fields not empty
        if (binding.waypointEtName.text.isNotEmpty() && binding.waypointEtLat.text.isNotEmpty() && binding.waypointEtLon.text.isNotEmpty()) {

            //try catch for writing errors
            try {

                val waypoint = hashMapOf(
                    "lat" to binding.waypointEtLat.text.toString().toDouble(),
                    "lon" to binding.waypointEtLon.text.toString().toDouble()
                )

                db.collection("waypoints").document(binding.waypointEtName.text.toString())
                    .set(waypoint)

                Toast.makeText(this,getString(R.string.writesuccess),Toast.LENGTH_SHORT).show()

                finish()

            } catch (e: Exception) {
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, getString(R.string.emptyfield), Toast.LENGTH_SHORT).show()
        }


    }
}