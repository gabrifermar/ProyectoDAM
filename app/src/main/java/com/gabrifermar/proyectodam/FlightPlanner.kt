package com.gabrifermar.proyectodam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var adapter: FlightPlannerAdapter
    private var wpnames = mutableListOf<String>()
    private var distances = mutableListOf<Double>()
    private var headings = mutableListOf<Int>()


    //TODO: pte como implementar introducir el ultimo punto en el recycler puesto que la lista se modifica dentro del listener
    //TODO: pte añadir favoritos y guardar en local para tener base de datos SQL, para poder implementar boton de busqueda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlightPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()

        //variables
        random = emptyList()
        val db = Firebase.firestore
        var name1: String
        var name2: String
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
            //clear list
            wpnames.clear()
            distances.clear()
            headings.clear()

            //enter db to get waypoints data to calculate
            db.collection("waypoints").get().addOnSuccessListener { document ->
                if (document != null) {
                    //background task for calculations
                    CoroutineScope(Dispatchers.IO).launch {
                        //get random numbers not repeated
                        do {
                            random =
                                (1..binding.flightplannerSbLegselector.progress + 2).map { (1..document.size()).random() }
                        } while (random.distinct().count() != random.size)

                        /*
                        //No funciona puesto que sale size 4 y solo hay 3 size de distancia y hdg
                        //get names of locations and storing them
                        for (element in random) {
                            db.collection("waypoints").document(element.toString()).get()
                                .addOnSuccessListener {
                                    wpnames.add(it.getString("name")!!)
                                }
                        }*/

                        //loop to get calculations between waypoints
                        for (i in 0..binding.flightplannerSbLegselector.progress) {
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

                                            var hdg = heading(
                                                toRadians(document1.getDouble("lat")!!),
                                                toRadians(document1.getDouble("lon")!!),
                                                toRadians(document2.getDouble("lat")!!),
                                                toRadians(document2.getDouble("lon")!!)
                                            )

                                            if (hdg < 0) {
                                                hdg += 360
                                            }

                                            wpnames.add(document1.getString("name")!!)
                                            distances.add(String.format("%.1f", d).toDouble())
                                            headings.add(hdg.roundToInt())


                                            adapter.notifyDataSetChanged()

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
        adapter = FlightPlannerAdapter(wpnames, distances, headings)
        binding.flightplannerRvFlightplan.adapter = adapter
    }
}

