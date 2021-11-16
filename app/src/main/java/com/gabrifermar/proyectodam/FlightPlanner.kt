package com.gabrifermar.proyectodam

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrifermar.proyectodam.databinding.ActivityFlightPlannerBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlin.math.*

class FlightPlanner : AppCompatActivity() {

    private lateinit var binding: ActivityFlightPlannerBinding
    private lateinit var random: List<Int>
    private lateinit var adapter: FlightPlannerAdapter
    private var wpnames = mutableListOf<String>()
    private var distances = mutableListOf<Double>()
    private var headings = mutableListOf<Int>()


    //TODO: pte añadir favoritos y guardar en local para tener base de datos SQL, para poder implementar boton de busqueda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlightPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //recycler
        initRecycler()

        //variables
        random = emptyList()
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
            flightplan()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun flightplan() {

        //variables
        val db = Firebase.firestore
        var name1 = ""
        var name2 = ""
        var lat1 = 0.0
        var lat2 = 0.0
        var lon1 = 0.0
        var lon2 = 0.0

        //clear list
        wpnames.clear()
        distances.clear()
        headings.clear()
        adapter.notifyDataSetChanged()

        //enter db to get waypoints data to calculate
        db.collection("waypoints").get().addOnSuccessListener() { document ->
            if (document != null) {
                //background task for calculations
                CoroutineScope(Dispatchers.IO).launch {
                    //get random numbers not repeated
                    do {
                        random =
                            (1..binding.flightplannerSbLegselector.progress + 2).map { (1..document.size()).random() }
                    } while (random.distinct().count() != random.size)

                    //loop to get calculations between waypoints
                    for (i in 0..binding.flightplannerSbLegselector.progress) {
                        db.collection("waypoints").whereEqualTo("ID", random[i]).get()
                            .addOnSuccessListener { document1 ->
                                db.collection("waypoints").whereEqualTo("ID", random[i + 1])
                                    .get()
                                    .addOnSuccessListener { document2 ->

                                        for (a in document1) {
                                            lat1 = a.getDouble("lat")!!
                                            lon1 = a.getDouble("lon")!!
                                            name1 = a.id
                                        }

                                        for (b in document2) {
                                            lat2 = b.getDouble("lat")!!
                                            lon2 = b.getDouble("lon")!!
                                            name2 = b.id
                                        }

                                        val d = distance(
                                            toRadians(lat1),
                                            toRadians(lon1),
                                            toRadians(lat2),
                                            toRadians(lon2)
                                        )

                                        var hdg = heading(
                                            toRadians(lat1),
                                            toRadians(lon1),
                                            toRadians(lat2),
                                            toRadians(lon2)
                                        )

                                        if (hdg < 0) {
                                            hdg += 360
                                        }


                                        if (i == random.size - 2) {

                                            wpnames.add(name1)
                                            distances.add(String.format("%.1f", d).toDouble())
                                            headings.add(hdg.roundToInt())
                                            wpnames.add(name2)
                                            distances.add(0.0)
                                            headings.add(0)
                                            adapter.notifyDataSetChanged()

                                        } else {

                                            wpnames.add(name1)
                                            distances.add(String.format("%.1f", d).toDouble())
                                            headings.add(hdg.roundToInt())
                                            adapter.notifyDataSetChanged()

                                        }
                                    }
                            }
                        //insert delay to let it retrieve data from firebase
                        delay(100)
                    }
                }
            }
        }
    }


    //calculate hdg between 2 coordinates
    private fun heading(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        return 90 - toDegrees(
            atan2(
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

    //Degrees to Radians
    private fun toRadians(a: Double): Double {
        return a * Math.PI / 180
    }

    //Radians to Degrees
    private fun toDegrees(a: Double): Double {
        return a * 180 / Math.PI
    }

    private fun initRecycler() {
        binding.flightplannerRvFlightplan.layoutManager = LinearLayoutManager(this)
        adapter = FlightPlannerAdapter(this,wpnames, distances, headings)
        binding.flightplannerRvFlightplan.adapter = adapter
    }
}


