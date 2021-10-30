package com.gabrifermar.proyectodam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gabrifermar.proyectodam.databinding.ActivityAdminBinding

class Admin : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //listeners
        binding.adminBtnNewuser.setOnClickListener {
            startActivity(Intent(this,NewUser::class.java))
        }

        binding.adminBtnAddwaypoint.setOnClickListener {
            startActivity(Intent(this,NewWaypoint::class.java))
        }


    }
}