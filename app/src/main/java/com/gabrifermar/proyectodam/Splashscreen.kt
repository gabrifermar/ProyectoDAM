package com.gabrifermar.proyectodam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.splashscreen.*

class Splashscreen : AppCompatActivity() {

    private val delay: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        //Declare animations
        val ttb = AnimationUtils.loadAnimation(this, R.anim.ttb)
        val btt = AnimationUtils.loadAnimation(this, R.anim.btt)

        //start animations
        logotxt.startAnimation(ttb)
        cloud.startAnimation(btt)

        Handler().postDelayed({
            startActivity(Intent(this,Home::class.java))
            finish()
        },delay)

    }
}