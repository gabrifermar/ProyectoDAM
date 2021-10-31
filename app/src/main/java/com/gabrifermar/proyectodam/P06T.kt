package com.gabrifermar.proyectodam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gabrifermar.proyectodam.databinding.ActivityP06tBinding
import com.gabrifermar.proyectodam.databinding.ActivityP28rBinding

class P06T : AppCompatActivity() {

    private lateinit var binding: ActivityP06tBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityP06tBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}