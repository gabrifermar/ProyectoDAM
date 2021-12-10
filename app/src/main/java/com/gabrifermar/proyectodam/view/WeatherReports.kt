package com.gabrifermar.proyectodam.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrifermar.proyectodam.model.api.API
import com.gabrifermar.proyectodam.model.ProyectoDAMapp
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.databinding.ActivityWeatherReportsBinding
import com.gabrifermar.proyectodam.model.adapter.MetarAdapter
import com.gabrifermar.proyectodam.model.room.Weather
import com.google.android.gms.location.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class WeatherReports : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherReportsBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var adapter: MetarAdapter
    private lateinit var date: String
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var metarlist = mutableListOf<String>()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //load chips
        loadIcao()

        //start reclycerview
        initRecycler()

        //backarrow
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //title
        supportActionBar!!.title=getString(R.string.meteo)

        //variable
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } else {
            Calendar.getInstance()
            SimpleDateFormat("dd/MM/yyy").format(Date())
        }

        //listeners
        startListeners()

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
                if (p0.locations.isNotEmpty()) {
                    lat = String.format("%.2f", p0.lastLocation.latitude).toDouble()
                    lon = String.format("%.2f", p0.lastLocation.longitude).toDouble()
                }
            }
        }

        //start location
        startLocationUpdates()
    }

    private fun startListeners() {
        binding.weatherCgIcao.setOnCheckedChangeListener { _, checkedId ->
            val chip = binding.weatherCgIcao.findViewById<Chip>(checkedId)
            binding.weatherCgIcao.removeView(chip)
        }

        binding.weatherBtnAdd.setOnClickListener {
            if (binding.weatherTxtInput.text.length == 4) {
                checkIcao(binding.weatherTxtInput.text.toString())
            } else {
                binding.weatherTxtInput.error = resources.getString(R.string.wrongicao)
                binding.weatherTxtInput.requestFocus()
            }
        }

        binding.weatherTxtInput.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (binding.weatherTxtInput.text.length == 4) {
                    checkIcao(binding.weatherTxtInput.text.toString())
                } else {
                    binding.weatherTxtInput.error = resources.getString(R.string.wrongicao)
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
            hideKeyboard(binding.weatherTxtInput)
            loadmetar(icaolist().joinToString(separator = ","), 1)
        }

        binding.weatherBtnTaf.setOnClickListener {
            hideKeyboard(binding.weatherTxtInput)
            loadtaf(icaolist().joinToString(separator = ","))
        }

        binding.weatherBtnHistorical.setOnClickListener {
            startActivity(Intent(this, WeatherHistorical::class.java).putExtra("mode", 1))
        }

        binding.weatherBtnFav.setOnClickListener {
            startActivity(Intent(this, WeatherHistorical::class.java).putExtra("mode", 2))
        }
    }


    private fun checkIcao(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getmetarcall().create(API::class.java)
                    .getMetar("$query/?x-api-key=d49660ce845e4f3db1fc469256")
                val metarcall = call.body()
                runOnUiThread {
                    if (metarcall?.results == 0) {
                        binding.weatherTxtInput.error = resources.getString(R.string.wrongicao)
                        binding.weatherTxtInput.requestFocus()
                    } else if (call.isSuccessful) {
                        addChip(binding.weatherTxtInput.text.toString().toUpperCase(Locale.ROOT))
                        binding.weatherTxtInput.text.clear()
                    }
                }
            } catch (e: UnknownHostException) {
                //host error (no internet) and show outside coroutine
                runOnUiThread {
                    Toast.makeText(
                        this@WeatherReports,
                        R.string.checkinternet,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } catch (e: com.google.gson.JsonSyntaxException) {
                runOnUiThread {
                    Toast.makeText(this@WeatherReports, R.string.emptyicao, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
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

    @SuppressLint("NotifyDataSetChanged")
    private fun loadmetar(query: String, mode: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            //try catch for errors
            try {
                //mode 1 = search stations
                if (mode == 1) {
                    val call = getmetarcall().create(API::class.java)
                        .getMetar("$query/?x-api-key=d49660ce845e4f3db1fc469256")
                    val metarcall = call.body()
                    runOnUiThread {
                        if (metarcall?.results == 0) {
                            binding.weatherTxtInput.error =
                                resources.getString(R.string.wrongicao)
                            binding.weatherTxtInput.requestFocus()
                        } else if (call.isSuccessful) {
                            val metars = metarcall?.data ?: emptyList()
                            metarlist.clear()
                            metarlist.addAll(metars)
                            adapter.notifyDataSetChanged()
                            addDb(metars, true)
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
                //host error (no internet) and show outside coroutine
                runOnUiThread {
                    Toast.makeText(
                        this@WeatherReports,
                        R.string.checkinternet,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } catch (e: com.google.gson.JsonSyntaxException) {
                runOnUiThread {
                    Toast.makeText(this@WeatherReports, R.string.emptyicao, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun addDb(metarlist: List<String>, ismetar: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val repository = applicationContext as ProyectoDAMapp
            for (metar in metarlist) {
                if (!repository.repository.isExists(metar)) {
                    val weather = Weather(0, date, metar, ismetar, fav = false)
                    repository.repository.insertone(weather)
                }
            }
        }
    }


    private fun gettafcall(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.checkwx.com/taf/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadtaf(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            //try catch for errors
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
                        addDb(metarlist, false)
                    }
                }
            } catch (e: UnknownHostException) {
                //host error (no internet) and show outside coroutine
                runOnUiThread {
                    Toast.makeText(
                        this@WeatherReports,
                        R.string.checkinternet,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } catch (e: com.google.gson.JsonSyntaxException) {
                runOnUiThread {
                    Toast.makeText(this@WeatherReports, R.string.emptyicao, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun initRecycler() {
        binding.weatherRvReports.layoutManager = LinearLayoutManager(this)
        adapter = MetarAdapter(metarlist, this)
        binding.weatherRvReports.adapter = adapter
    }

    private fun hideKeyboard(v: View) {
        val inputMethodManager =
            applicationContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    //save chips
    private fun saveIcao() {
        val sharedPref = this.getSharedPreferences("weather", Context.MODE_PRIVATE)
        sharedPref.edit().putString("ICAO", icaolist().joinToString(separator = ",")).apply()
    }

    //load chips
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        saveIcao()
        finish()
        return super.onOptionsItemSelected(item)
    }

}
