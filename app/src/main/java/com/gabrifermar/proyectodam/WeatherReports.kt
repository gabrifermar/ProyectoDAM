package com.gabrifermar.proyectodam

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_weather_reports.*

class WeatherReports : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_reports)

        //variable
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        weather_btn_near.setOnClickListener {
            getLastLocation()
        }

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
                Log.d("hola", "ok enabled")
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    val location = it.result
                    if (location == null) {
                        getLocation()
                    } else {
                        //TODO: call api close stations and reclycler view adapter and update pos
                        Log.d("location", location.latitude.toString())
                        Log.d("location", location.longitude.toString())
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

/*
    private fun checkPermission(permission: String, name: String, requestCode: Int) {
        Log.d("permission","ok")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d("permission","granted")
                    locationinfo()
                    //ok
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission,
                    name,
                    requestCode
                )

                else -> ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
    }*/


    /*
    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage(R.string.locationerror)
            setTitle("Permision required")
            setPositiveButton("ok") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@WeatherReports,
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }*/
}