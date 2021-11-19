package com.gabrifermar.proyectodam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrifermar.proyectodam.databinding.ActivityWeatherHistoricalBinding
import com.gabrifermar.proyectodam.model.WeatherHistoricalAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherHistorical : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherHistoricalBinding
    private lateinit var adapter: WeatherHistoricalAdapter
    private lateinit var room: ProyectoDAMapp
    private var date = mutableListOf<String>()
    private var data = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherHistoricalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //variable
        room = application as ProyectoDAMapp

        //recycler
        initRecycler()

        //listeners
        startListeners()
    }

    private fun startListeners() {
        binding.weatherHistoricalBtnMetar.setOnClickListener {
            retrieveMetar()
        }

        binding.weatherHistoricalBtnTaf.setOnClickListener {
            retrieveTaf()
        }
    }

    private fun retrieveTaf() {
        date.clear()
        data.clear()


       // val metar = room.roomdb.weatherDao().allTaf()



    }

    private fun retrieveMetar() {
        date.clear()
        data.clear()

        val roomdb=applicationContext as ProyectoDAMapp

        CoroutineScope(Dispatchers.IO).launch {

            //val metar = roomdb.roomdb.weatherDao().allTaf()


        }
    }

    private fun initRecycler() {
        binding.weatherRvHistorical.layoutManager = LinearLayoutManager(this)
        adapter = WeatherHistoricalAdapter(data, date)
        binding.weatherRvHistorical.adapter = adapter
    }
}