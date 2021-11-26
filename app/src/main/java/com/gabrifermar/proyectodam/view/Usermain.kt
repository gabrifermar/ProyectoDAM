package com.gabrifermar.proyectodam.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.databinding.ActivityUsermainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Usermain : AppCompatActivity() {

    private lateinit var binding: ActivityUsermainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsermainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //variable
        auth = FirebaseAuth.getInstance()

        //write test grades to sharedPref
        flightprogress()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_usermain)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.userFragment,
                R.id.fragment_user_tools,
                R.id.navigation_notifications,
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    private fun flightprogress() {
        val sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE)
        val db = Firebase.firestore
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            if (it.getDouble("C172grade") != null) {
                sharedPref.edit().putInt("C172grade", it.getDouble("C172grade")!!.toInt()).apply()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val userdata = getSharedPreferences("user", Context.MODE_PRIVATE)
        val userdataencrypted = getSharedPreferences("user_encrypted", Context.MODE_PRIVATE)

        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, Settings::class.java))
                true
            }
            R.id.logout -> {
                auth.signOut()
                startActivity(Intent(this, Home::class.java))
                userdata.edit().clear().apply()
                userdataencrypted.edit().clear().apply()
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        flightprogress()
        super.onResume()
    }
}