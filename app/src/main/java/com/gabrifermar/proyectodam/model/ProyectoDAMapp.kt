package com.gabrifermar.proyectodam.model

import android.app.Application
import com.gabrifermar.proyectodam.model.room.WeatherDB
import com.gabrifermar.proyectodam.model.room.WeatherRepository
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ProyectoDAMapp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { WeatherDB.getDatabase(this, applicationScope) }
    val repository by lazy { WeatherRepository(database.weatherDao()) }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}