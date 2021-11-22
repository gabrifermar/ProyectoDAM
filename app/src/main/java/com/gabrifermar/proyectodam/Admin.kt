package com.gabrifermar.proyectodam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gabrifermar.proyectodam.databinding.ActivityAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Admin : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //variables
        auth=Firebase.auth

        //listeners
        startListeners()

    }

    private fun startListeners(){
        binding.adminBtnNewuser.setOnClickListener {
            startActivity(Intent(this,NewUser::class.java))
        }

        binding.adminBtnAddwaypoint.setOnClickListener {
            startActivity(Intent(this,NewWaypoint::class.java))
        }

        binding.adminBtnNewquestion.setOnClickListener {
            startActivity(Intent(this,NewQuestion::class.java))
        }
    }
}