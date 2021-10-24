package com.gabrifermar.proyectodam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.gabrifermar.proyectodam.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.gabrifermar.proyectodam.Usermain as Usermain

class Home : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private val metarlist = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment_container, Settings.newInstance())

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_login
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //TODO: cuando carga directo la activity de User al estar logado, no carga el nuevo metar y no actualiza
        loadmetar("LEVS")



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                settings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun settings() {
        startActivity(Intent(this, Settings::class.java))
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun login(view: View) {

        //variable
        auth = Firebase.auth
        val db = Firebase.firestore

        //val gsReference = storage.getReferenceFromUrl("gs://proyectoaep-d6bc6.appspot.com/Certificado.pdf")

        auth.signInWithEmailAndPassword(
            username.text.toString() + "@hola.com",
            password.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    /*
                    db.collection("users").document(auth.currentUser!!.uid).get()
                        .addOnSuccessListener { document ->
                            if (document != null) {

                                val sharedPref =
                                    this.getSharedPreferences("user", Context.MODE_PRIVATE)
                                sharedPref.edit()
                                    .putString("username", document.getString("username")).apply()


                                //check user settings
                                if (document.getBoolean("subjects") == true) {
                                    //check fields
                                }

                            }
                        }*/

                    startActivity(Intent(this, Usermain::class.java))

                    //admin access
                } else if (username.text.toString() == "admin" && password.text.toString() == "admin") {
                    startActivity(Intent(this, Admin::class.java))


                    //error
                } else {
                    Toast.makeText(this, "error", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun getmetarcall(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.checkwx.com/metar/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    //TODO: pte implementar escritura en db para historico posible fav en local
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

                    val sharedPref = this@Home.getSharedPreferences("user", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("metar", metarlist[0]).apply()


                    //user_txt_metar.text = metarlist[0]
                }
            }
        }
    }

}