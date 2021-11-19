package com.gabrifermar.proyectodam.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrifermar.proyectodam.ProyectoDAMapp
import com.gabrifermar.proyectodam.databinding.ActivityWeatherHistoricalBinding
import com.gabrifermar.proyectodam.model.WeatherHistoricalAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherHistorical : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherHistoricalBinding
    private lateinit var adapter: WeatherHistoricalAdapter
    private var date = mutableListOf<String>()
    private var data = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherHistoricalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getExtras
        val mode = intent.getIntExtra("mode", 1)

        //recycler
        initRecycler()

        //listeners
        startListeners(mode)
    }

    private fun startListeners(mode: Int) {
        binding.weatherHistoricalBtnMetar.setOnClickListener {
            when (mode) {
                1 -> retrieveMetar()
                2 -> retrieveMetarFav()
            }
        }

        binding.weatherHistoricalBtnTaf.setOnClickListener {
            when (mode) {
                1 -> retrieveTaf()
                2 -> retrieveTafFav()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveTafFav() {
        val repository = applicationContext as ProyectoDAMapp
        date.clear()
        data.clear()

        CoroutineScope(Dispatchers.IO).launch {
            val metarlist=repository.repository.allTafFav()
            for (metar in metarlist){
                runOnUiThread{
                    date.add(metar.date)
                    data.add(metar.data)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveMetarFav() {
        val repository = applicationContext as ProyectoDAMapp
        date.clear()
        data.clear()

        CoroutineScope(Dispatchers.IO).launch {
            val metarlist=repository.repository.allMetarFav()
            for (metar in metarlist){
                runOnUiThread{
                    date.add(metar.date)
                    data.add(metar.data)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveTaf() {
        //variable
        val repository = applicationContext as ProyectoDAMapp
        date.clear()
        data.clear()

        CoroutineScope(Dispatchers.IO).launch {
            val metarlist = repository.repository.allTaf()
            for (metar in metarlist) {
                runOnUiThread {
                    date.add(metar.date)
                    data.add(metar.data)
                    adapter.notifyDataSetChanged()
                }
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun retrieveMetar() {
        //variable
        val repository = applicationContext as ProyectoDAMapp
        date.clear()
        data.clear()

        CoroutineScope(Dispatchers.IO).launch {
            val metarlist = repository.repository.allMetar()
            for (metar in metarlist) {
                runOnUiThread {
                    date.add(metar.date)
                    data.add(metar.data)
                    adapter.notifyDataSetChanged()
                }
            }

        }
    }

    private fun initRecycler() {
        binding.weatherRvHistorical.layoutManager = LinearLayoutManager(this)
        adapter = WeatherHistoricalAdapter(data, date)
        binding.weatherRvHistorical.adapter = adapter
    }
}