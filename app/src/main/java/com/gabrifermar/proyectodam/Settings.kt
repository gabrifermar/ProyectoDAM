package com.gabrifermar.proyectodam

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPref = this.getSharedPreferences("mode",Context.MODE_PRIVATE)

        settings_btn_darkmode.isChecked = sharedPref.getBoolean("darkmode",false)

        settings_btn_darkmode.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPref.edit().putBoolean("darkmode",true).apply()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPref.edit().putBoolean("darkmode",false).apply()
            }
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

}