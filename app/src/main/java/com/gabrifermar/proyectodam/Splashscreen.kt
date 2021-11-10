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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class Splashscreen : AppCompatActivity() {

    private val delay: Long = 2000
    private lateinit var auth: FirebaseAuth
    private val metarlist = mutableListOf<String>()


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
            setLanguage(this, sharedPref.getString("language", "en")!!)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setLanguage(this, sharedPref.getString("language", "en")!!)
        }

        //check language
        setLanguage(this, sharedPref.getString("language", "en")!!)


        Handler().postDelayed({
            //check if user already logged
            if (auth.currentUser != null) {
                loadmetar("LEVS")
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


    internal fun loadmetar(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getmetarcall().create(API::class.java)
                .getMetar("$query/?x-api-key=d49660ce845e4f3db1fc469256")
            val levs = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    val metars = levs?.data ?: emptyList()
                    metarlist.clear()
                    metarlist.addAll(metars)
                    val sharedPref = this@Splashscreen.getSharedPreferences("user", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("metar", metarlist[0]).apply()
                }
            }
        }
    }

}