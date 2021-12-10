package com.gabrifermar.proyectodam.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gabrifermar.proyectodam.databinding.ActivityP28rBinding

class P28R : AppCompatActivity() {

    private lateinit var binding:ActivityP28rBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityP28rBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}