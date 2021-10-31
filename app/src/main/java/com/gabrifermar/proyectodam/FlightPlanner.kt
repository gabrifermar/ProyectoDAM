package com.gabrifermar.proyectodam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import com.gabrifermar.proyectodam.databinding.ActivityFlightPlannerBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.*

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
        binding.flightplannerTvSeekbarvalue.text = 2.toString()

        binding.flightplannerSbLegselector.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.flightplannerTvSeekbarvalue.text = (progress + 2).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        binding.flightplannerBtnGenerate.setOnClickListener {


            db.collection("waypoints").get().addOnSuccessListener { document ->
                if (document != null) {

                    CoroutineScope(Dispatchers.IO).launch {

                        do {
                            random =
                                (1..binding.flightplannerSbLegselector.progress + 2).map { (1..document.size()).random() }
                        } while (random.distinct().count() != random.size)

                        Log.d("mates", "distintos= ${random.distinct().count()}")


                        for (i in 0 until binding.flightplannerSbLegselector.progress + 1) {
                            Log.d("mates", i.toString())
                            Log.d("mates", random[i].toString())
                            Log.d("mates", random[i + 1].toString())
                            db.collection("waypoints").document(random[i].toString()).get()
                                .addOnSuccessListener { document1 ->
                                    db.collection("waypoints").document(random[i + 1].toString())
                                        .get()
                                        .addOnSuccessListener { document2 ->
                                            name1 = document1.getString("name")!!
                                            name2 = document2.getString("name")!!

                                            val d = distance(
                                                toRadians(document1.getDouble("lat")!!),
                                                toRadians(document1.getDouble("lon")!!),
                                                toRadians(document2.getDouble("lat")!!),
                                                toRadians(document2.getDouble("lon")!!)
                                            )

                                            val hdg = heading(
                                                toRadians(document1.getDouble("lat")!!),
                                                toRadians(document1.getDouble("lon")!!),
                                                toRadians(document2.getDouble("lat")!!),
                                                toRadians(document2.getDouble("lon")!!)
                                            )

                                            Log.d(
                                                "mates",
                                                "$name1 to $name2 : Hdg=$hdg and distance=$d"
                                            )
                                        }
                                }
                        }
                    }
                }


            }
        }


    }

    //calculate hdg between 2 coordinates
    private fun heading(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        return 90 - toDegrees(
            atan2(
                //(sin(toRadians(lon2) - toRadians(lon1))),
                ((cos(lat1) * tan(lat2)) - (sin(lat1) * cos(lon2 - lon1))),
                (sin(lon2 - lon1))
            )
        )
    }


    //calculate distance between 2 coordinates
    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        return 3443.9184 * acos(
            (sin(lat1) * sin(lat2)) + (cos(lat1) * cos(lat2) * cos(lon2 - lon1))
        )
    }

    private fun toRadians(a: Double): Double {
        return a * Math.PI / 180
    }

    private fun toDegrees(a: Double): Double {
        return a * 180 / Math.PI
    }
}

