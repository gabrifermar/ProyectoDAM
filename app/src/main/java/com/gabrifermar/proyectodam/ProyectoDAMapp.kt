package com.gabrifermar.proyectodam

import android.app.Application
import com.google.android.gms.ads.MobileAds

class ProyectoDAMapp: Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}