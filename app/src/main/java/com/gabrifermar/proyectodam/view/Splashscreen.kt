package com.gabrifermar.proyectodam.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.gabrifermar.proyectodam.model.api.API
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.databinding.SplashscreenBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.splashscreen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException
import java.util.*

class Splashscreen : AppCompatActivity() {

    private val delay: Long = 2000
    private lateinit var auth: FirebaseAuth
    private val metarlist = mutableListOf<String>()
    private lateinit var binding: SplashscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            setLanguage(this, sharedPref.getString("language", "en")!!)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setLanguage(this, sharedPref.getString("language", "en")!!)
        }

        //check language
        setLanguage(this, sharedPref.getString("language", "en")!!)

        //precharge metar into sharedpref for later use
        if (auth.currentUser != null) {
            loadmetar()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            //check if user already logged
            if (auth.currentUser != null) {
                startActivity(Intent(this, Usermain::class.java))
                finish()
            } else {
                startActivity(Intent(this, Home::class.java))
                finish()
            }
        }, delay)
    }

    private fun setLanguage(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    private fun getmetarcall(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.checkwx.com/metar/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    private fun loadmetar() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getmetarcall().create(API::class.java)
                    .getMetar("LEVS/?x-api-key=d49660ce845e4f3db1fc469256")
                val levs = call.body()
                runOnUiThread {
                    if (call.isSuccessful) {
                        val metars = levs?.data ?: emptyList()
                        metarlist.clear()
                        metarlist.addAll(metars)
                        val sharedPref =
                            this@Splashscreen.getSharedPreferences("user", Context.MODE_PRIVATE)
                        sharedPref.edit().putString("metar", metarlist[0]).apply()
                    }
                }
            } catch (e: UnknownHostException) {
                //No internet
            }

        }
    }
}