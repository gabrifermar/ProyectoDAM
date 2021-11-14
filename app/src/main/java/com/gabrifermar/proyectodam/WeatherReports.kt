package com.gabrifermar.proyectodam

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrifermar.proyectodam.databinding.ActivityWeatherReportsBinding
import com.google.android.gms.location.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.net.UnknownHostException

class WeatherReports : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherReportsBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var adapter: MetarAdapter
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var metarlist = mutableListOf<String>()

    //TODO: pte implementar escritura en db para historico posible fav en local


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //load chips
        loadIcao()

        //start reclycerview
        initRecycler()

        //variable
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //listeners
        binding.weatherCgIcao.setOnCheckedChangeListener { _, checkedId ->
            val chip = binding.weatherCgIcao.findViewById<Chip>(checkedId)
            binding.weatherCgIcao.removeView(chip)
        }

        binding.weatherBtnAdd.setOnClickListener {

            //TODO: posible comprobacion de ICAO antes de añadir, llamando a la API

            if (binding.weatherTxtInput.text.length == 4) {
                addChip(binding.weatherTxtInput.text.toString().uppercase())
                binding.weatherTxtInput.text.clear()
            } else {
                binding.weatherTxtInput.error = resources.getString(R.string.emptyfield)
                binding.weatherTxtInput.requestFocus()
            }
        }

        binding.weatherTxtInput.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (binding.weatherTxtInput.text.length == 4) {
                    addChip(binding.weatherTxtInput.text.toString().uppercase())
                    binding.weatherTxtInput.text.clear()
                } else {
                    binding.weatherTxtInput.error = resources.getString(R.string.emptyfield)
                    binding.weatherTxtInput.requestFocus()
                }
                return@setOnKeyListener true
            }
            false
        }

        binding.weatherBtnNear.setOnClickListener {
            binding.weatherTxtInput.clearFocus()
            getLastLocation()
            loadmetar("", 2)
        }

        binding.weatherBtnMetar.setOnClickListener {
            binding.weatherTxtInput.clearFocus()
            hideKeyboard(this, binding.weatherTxtInput)
            loadmetar(icaolist().joinToString(separator = ","), 1)
        }

        binding.weatherBtnTaf.setOnClickListener {
            hideKeyboard(this, binding.weatherTxtInput)
            loadtaf(icaolist().joinToString(separator = ","))
        }

        locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 20000
            smallestDisplacement = 100f
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            isWaitForAccurateLocation = true
        }


        //update pos
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                if (p0.locations.isNotEmpty()) {
                    lat = String.format("%.2f", p0.lastLocation.latitude).toDouble()
                    lon = String.format("%.2f", p0.lastLocation.longitude).toDouble()
                }
            }
        }

        startLocationUpdates()

    }

    private fun icaolist(): List<String> {
        val list = mutableListOf<String>()
        for (i in 0 until binding.weatherCgIcao.size) {
            val chip =
                binding.weatherCgIcao.findViewById<Chip>(binding.weatherCgIcao.getChildAt(i).id)
            list.add(chip.text.toString())
        }
        return list
    }

    private fun addChip(text: String) {
        //variables
        val chip = Chip(this)
        val drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Metarchip)

        //setup chip
        chip.setChipDrawable(drawable)
        chip.text = text
        chip.isCloseIconVisible = true
        binding.weatherCgIcao.addView(chip)
    }


    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            101
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            (LocationManager.NETWORK_PROVIDER)
        )
    }

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    val location = it.result
                    if (location == null) {
                        getLocation()
                    }
                }
            } else {
                getLocation()
            }
        } else {
            requestPermission()
        }
    }

    private fun getLocation() {
        startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun getmetarcall(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.checkwx.com/metar/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadmetar(query: String, mode: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //mode 1 = search stations
                if (mode == 1) {
                    val call = getmetarcall().create(API::class.java)
                        .getMetar("$query/?x-api-key=d49660ce845e4f3db1fc469256")
                    val metarcall = call.body()
                    runOnUiThread {
                        if (metarcall?.results == 0) {
                            binding.weatherTxtInput.error = resources.getString(R.string.wrongicao)
                            binding.weatherTxtInput.requestFocus()
                        } else if (call.isSuccessful) {
                            val metars = metarcall?.data ?: emptyList()
                            metarlist.clear()
                            metarlist.addAll(metars)
                            adapter.notifyDataSetChanged()
                        }
                    }

                    //mode 2 = near stations
                } else if (mode == 2) {
                    val call = getmetarcall().create(API::class.java)
                        .getMetar("lat/$lat/lon/$lon/?x-api-key=d49660ce845e4f3db1fc469256")
                    val levs = call.body()
                    runOnUiThread {
                        if (call.isSuccessful) {
                            val metars = levs?.data ?: emptyList()
                            metarlist.clear()
                            metarlist.addAll(metars)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            } catch (e: UnknownHostException) {
                //TODO:averiguar pq crashea cuando hay que enseñar el toast, y pq en taf no funciona el catch
                //Toast.makeText(this@WeatherReports, e.toString(), Toast.LENGTH_SHORT)
                  //  .show()
                Log.e("error",e.toString())
            }
        }
    }

    private fun gettafcall(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.checkwx.com/taf/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadtaf(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = gettafcall().create(API::class.java)
                    .getMetar("$query/?x-api-key=d49660ce845e4f3db1fc469256")
                val tafcall = call.body()
                runOnUiThread {
                    if (call.isSuccessful) {
                        val tafs = tafcall?.data ?: emptyList()
                        metarlist.clear()
                        metarlist.addAll(tafs)
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: UnknownHostException) {
                Toast.makeText(this@WeatherReports, "hola", Toast.LENGTH_SHORT).show()
                Log.e("error",e.toString())
            }
        }
    }

    private fun initRecycler() {
        binding.weatherRvReports.layoutManager = LinearLayoutManager(this)
        adapter = MetarAdapter(metarlist)
        binding.weatherRvReports.adapter = adapter
    }

    private fun hideKeyboard(context: Context, v: View) {
        val inputMethodManager =
            applicationContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    private fun saveIcao() {
        val sharedPref = this.getSharedPreferences("weather", Context.MODE_PRIVATE)
        sharedPref.edit().putString("ICAO", icaolist().joinToString(separator = ",")).apply()
    }

    private fun loadIcao() {
        val sharedPref = this.getSharedPreferences("weather", Context.MODE_PRIVATE)
        val icaos = sharedPref.getString("ICAO", null)

        //check for empty values
        if (icaos != null) {
            if (icaos.isNotEmpty()) {
                icaos.split(",").map { it.trim() }.forEach { addChip(it) }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    override fun onRestart() {
        super.onRestart()
        startLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveIcao()
        finish()
    }

}
