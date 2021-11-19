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
        binding.adminBtnNewuser.setOnClickListener {
            auth.currentUser?.delete()
            startActivity(Intent(this,NewUser::class.java))
        }

        binding.adminBtnAddwaypoint.setOnClickListener {
            startActivity(Intent(this,NewWaypoint::class.java))
        }
    }

    override fun onRestart() {
        super.onRestart()
        auth.signInAnonymously()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        auth.currentUser?.delete()
    }
}