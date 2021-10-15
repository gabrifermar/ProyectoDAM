package com.gabrifermar.proyectodam

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View.VISIBLE
import android.widget.Toast
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

class Usermain : AppCompatActivity() {

    private lateinit var binding: ActivityUsermainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Declare variable
        auth = Firebase.auth
        val db = Firebase.firestore


        binding = ActivityUsermainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_usermain)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.userFragment,
                R.id.navigation_dashboard,
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


        //TODO: pte recoger datos de Firestore para crear distinto user interface
        //get inside Firestore database
        db.collection("users").document(auth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {


                    //TODO: posible gestion de bienvenida en background o pantalla de carga hasta que carge todo ok
                    user_txt_username.text= getString(R.string.welcome, document.getString("username"))
                    user_txt_username.visibility=VISIBLE

                    //check user settings
                    if (document.getBoolean("subjects") == true) {
                        Log.d(TAG, "true")
                    } else {
                        Log.d(TAG, "false")
                    }


                    Log.d(TAG, document.data.toString())
                    //C172=true, subjects=true, P28R=true, P06T=true, username=hola
                }


            }
    }
}