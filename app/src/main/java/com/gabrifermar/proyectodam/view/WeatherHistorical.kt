package com.gabrifermar.proyectodam.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrifermar.proyectodam.model.ProyectoDAMapp
import com.gabrifermar.proyectodam.databinding.ActivityWeatherHistoricalBinding
import com.gabrifermar.proyectodam.model.adapter.WeatherHistoricalAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherHistorical : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherHistoricalBinding
    private lateinit var adapter: WeatherHistoricalAdapter
    private var date = mutableListOf<String>()
    private var data = mutableListOf<String>()
    private var adaptermode = mutableListOf<Int>()
    private var index = 0

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

        //backarrow
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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

    private fun retrieveTafFav() {
        val repository = applicationContext as ProyectoDAMapp
        val size = date.size

        //reset recyclerview
        date.clear()
        data.clear()
        adaptermode.clear()
        adapter.notifyItemRangeRemoved(0, size)
        index = 0

        CoroutineScope(Dispatchers.IO).launch {
            val metarlist = repository.repository.allTafFav()
            for (metar in metarlist) {
                runOnUiThread {
                    date.add(metar.date)
                    data.add(metar.data)
                    adaptermode.add(1)
                    adapter.notifyItemInserted(index)
                }
            }
        }
    }

    private fun retrieveMetarFav() {
        //variable
        val repository = applicationContext as ProyectoDAMapp
        val size = date.size

        //reset recyclerview
        date.clear()
        data.clear()
        adaptermode.clear()
        adapter.notifyItemRangeRemoved(0, size)
        index = 0

        CoroutineScope(Dispatchers.IO).launch {
            val metarlist = repository.repository.allMetarFav()
            for (metar in metarlist) {
                runOnUiThread {
                    date.add(metar.date)
                    data.add(metar.data)
                    adaptermode.add(1)
                    adapter.notifyItemInserted(index)
                }
            }
        }
    }

    private fun retrieveTaf() {
        //variable
        val repository = applicationContext as ProyectoDAMapp
        val size = date.size

        //reset recyclerview
        date.clear()
        data.clear()
        adaptermode.clear()
        adapter.notifyItemRangeRemoved(0, size)
        index = 0

        CoroutineScope(Dispatchers.IO).launch {
            val metarlist = repository.repository.allTaf()
            for (metar in metarlist) {
                runOnUiThread {
                    date.add(metar.date)
                    data.add(metar.data)
                    adaptermode.add(2)
                    adapter.notifyItemInserted(index)
                }
            }

        }
    }

    private fun retrieveMetar() {
        //variable
        val repository = applicationContext as ProyectoDAMapp
        val size = date.size

        //reset recyclerview
        date.clear()
        data.clear()
        adaptermode.clear()
        adapter.notifyItemRangeRemoved(0, size)
        index = 0

        CoroutineScope(Dispatchers.IO).launch {
            val metarlist = repository.repository.allMetar()
            for (metar in metarlist) {
                runOnUiThread {
                    date.add(metar.date)
                    data.add(metar.data)
                    adaptermode.add(2)
                    adapter.notifyItemInserted(index)
                }

            }

        }
    }

    private fun initRecycler() {
        binding.weatherRvHistorical.layoutManager = LinearLayoutManager(this)
        adapter = WeatherHistoricalAdapter(data, date, adaptermode)
        binding.weatherRvHistorical.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}