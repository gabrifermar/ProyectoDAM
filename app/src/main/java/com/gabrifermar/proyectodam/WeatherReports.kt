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
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrifermar.proyectodam.databinding.ActivityWeatherReportsBinding
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherReports : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherReportsBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var adapter: MetarAdapter
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var metarlist = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()


        //variable
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        binding.weatherBtnNear.setOnClickListener {
            getLastLocation()
            loadmetar("", 2)
        }

        binding.weatherBtnMetar.setOnClickListener {
            //Check empty edit text
            if (binding.weatherTxtInput.text.isEmpty()) {
                binding.weatherTxtInput.error = resources.getString(R.string.emptyfield)
                binding.weatherTxtInput.requestFocus()
            } else {
                hideKeyboard(this, binding.weatherTxtInput)
                binding.weatherTxtInput.clearFocus()
                loadmetar(binding.weatherTxtInput.text.toString(), 1)
            }
        }

        binding.weatherBtnTaf.setOnClickListener {
            if (binding.weatherTxtInput.text.isEmpty()) {
                binding.weatherTxtInput.error = resources.getString(R.string.emptyfield)
                binding.weatherTxtInput.requestFocus()
            } else {
                hideKeyboard(this, binding.weatherTxtInput)
                binding.weatherTxtInput.clearFocus()
                loadtaf(binding.weatherTxtInput.text.toString())
            }

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

    internal fun loadmetar(query: String, mode: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            //mode 1 = search stations
            if (mode == 1) {
                val call = getmetarcall().create(API::class.java)
                    .getMetar("$query/?x-api-key=d49660ce845e4f3db1fc469256")
                val metarcall = call.body()
                runOnUiThread {
                    if(metarcall?.results==0){
                        binding.weatherTxtInput.error=resources.getString(R.string.wrongicao)
                        binding.weatherTxtInput.requestFocus()
                    }else if (call.isSuccessful) {
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
        }
    }

    private fun gettafcall(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.checkwx.com/taf/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    internal fun loadtaf(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
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
        finish()
    }

}
