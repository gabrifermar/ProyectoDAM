package com.gabrifermar.proyectodam

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View.VISIBLE
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.gabrifermar.proyectodam.databinding.ActivityUsermainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_usermain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Usermain : AppCompatActivity() {

    private lateinit var binding: ActivityUsermainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val metarlist = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUsermainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_usermain)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.userFragment,
                R.id.fragment_user_tools,
                R.id.navigation_notifications,
                R.id.vuelo
            )
        )

        //Añadir opciones al nav navigator
        //navView.menu.findItem(R.id.vuelo).isVisible = true

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        //check user

/*
        val gmmIntentUri =
            Uri.parse("geo:0,0?q=Aerotec, Madrid, España")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)*/




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
                    //TODO finalizar implementar API de metar
                    user_txt_metar.text = metarlist[0]
                }
            }
        }
    }
}