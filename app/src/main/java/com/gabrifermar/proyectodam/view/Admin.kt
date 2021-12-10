package com.gabrifermar.proyectodam.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gabrifermar.proyectodam.databinding.ActivityAdminBinding

class Admin : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //listeners
        startListeners()

    }

    private fun startListeners() {
        binding.adminBtnNewuser.setOnClickListener {
            startActivity(Intent(this, NewUser::class.java))
        }

        binding.adminBtnAddwaypoint.setOnClickListener {
            startActivity(Intent(this, NewWaypoint::class.java))
        }

        binding.adminBtnNewquestion.setOnClickListener {
            startActivity(Intent(this, NewQuestion::class.java))
        }

        binding.adminBtnNewnews.setOnClickListener {
            startActivity(Intent(this, NewNews::class.java))
        }
    }
}