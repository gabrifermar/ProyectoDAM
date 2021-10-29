package com.gabrifermar.proyectodam

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.splashscreen.*

class Splashscreen : AppCompatActivity() {

    private val delay: Long = 2000
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        //variable
        auth = FirebaseAuth.getInstance()


        //Declare animations
        val ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)
        val btt = AnimationUtils.loadAnimation(this, R.anim.btt)

        //start animations
        logotxt.startAnimation(ttb)
        cloud.startAnimation(btt)

        //check dark mode

        val sharedPref = this.getSharedPreferences("mode", Context.MODE_PRIVATE)
        if (sharedPref.getBoolean("darkmode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }


        Handler().postDelayed({
            if (auth.currentUser != null) {
                startActivity(Intent(this, Usermain::class.java))
                finish()
            } else {
                startActivity(Intent(this, Home::class.java))
                finish()
            }
        }, delay)


    }

}