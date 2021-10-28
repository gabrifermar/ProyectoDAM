package com.gabrifermar.proyectodam

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.gabrifermar.proyectodam.databinding.ActivityFlightMenuBinding

class FlightMenu : AppCompatActivity() {

    private lateinit var binding: ActivityFlightMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlightMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = this.getSharedPreferences("user", Context.MODE_PRIVATE)


        if (!sharedPref.getBoolean("C172", false)) {
            binding.flightmenuTxtC172progress.visibility = View.INVISIBLE
            binding.flightmenuIvLockC172.visibility = View.VISIBLE
            binding.flightmenuPbC172.visibility = View.INVISIBLE
        }

        if (!sharedPref.getBoolean("P28R", false)) {
            binding.flightmenuTxtP28Rprogress.visibility = View.INVISIBLE
            binding.flightmenuIvLockP28R.visibility = View.VISIBLE
            binding.flightmenuPbP28R.visibility = View.INVISIBLE
        }

        if (!sharedPref.getBoolean("P06T", false)) {
            binding.flightmenuTxtP06Tprogress.visibility = View.INVISIBLE
            binding.flightmenuIvLockP06T.visibility = View.VISIBLE
            binding.flightmenuPbP06T.visibility = View.INVISIBLE
        }



        //listeners
        binding.flightmenuCvC172.setOnClickListener {

        }

        binding.flightmenuCvP28R.setOnClickListener {

        }

        binding.flightmenuCvP06T.setOnClickListener {

        }

    }
}