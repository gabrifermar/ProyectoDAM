package com.gabrifermar.proyectodam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.gabrifermar.proyectodam.databinding.ActivityFlightPlannerBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.cosh
import kotlin.math.sin
import kotlin.random.Random

class FlightPlanner : AppCompatActivity() {

    private lateinit var binding: ActivityFlightPlannerBinding
    private lateinit var random: List<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlightPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //variables
        random = emptyList()
        val db = Firebase.firestore
        var name1 = ""
        var name2 = ""
        var lat1 = 0.0
        var lat2 = 0.0
        var lon1 = 0.0
        var lon2 = 0.0

        binding.flightplannerBtnGenerate.setOnClickListener {


            db.collection("waypoints").get().addOnSuccessListener { document ->
                if (document != null) {
                    random =
                        (1..binding.flightplannerSbLegselector.progress).map { (1..document.size()).random() }



                    //TODO: revision, loop bien, parece problema de los listeners
                    for (i in 0 until binding.flightplannerSbLegselector.progress-1) {
                        Log.d("mates",i.toString())
                        Log.d("mates",random[i].toString())
                        db.collection("waypoints").document(random[i].toString()).get()
                            .addOnSuccessListener { document1 ->
                                lat1 = document1.getDouble("lat")!!
                                lon1 = document1.getDouble("lon")!!
                                name1 = document1.getString("name")!!

                                db.collection("waypoints").document(random[i+1].toString()).get()
                                    .addOnSuccessListener { document2 ->
                                        lat2 = document2.getDouble("lat")!!
                                        lon2 = document2.getDouble("lon")!!
                                        name2 = document2.getString("name")!!

                                        val d = 3443.9184 * acos(
                                            (sin(toRadians(lat1)) * sin(toRadians(lat2))) + (cos(
                                                toRadians(lat1)
                                            ) * cos(toRadians(lat2)) * cos(
                                                toRadians(lon2) - toRadians(
                                                    lon1
                                                )
                                            ))
                                        )

                                        Log.d("mates", "$name1 to $name2 is $d")

                                    }
                            }
                    }
                }


            }
        }


    }

    private fun toRadians(a: Double): Double {
        return a * Math.PI / 180
    }
}