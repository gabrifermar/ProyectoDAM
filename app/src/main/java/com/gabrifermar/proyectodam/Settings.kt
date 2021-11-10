package com.gabrifermar.proyectodam

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import com.gabrifermar.proyectodam.databinding.ActivitySettingsBinding
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*
import android.os.Build
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources


class Settings : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = this.getSharedPreferences("mode", Context.MODE_PRIVATE)

        //Dark mode
        settings_btn_darkmode.isChecked = sharedPref.getBoolean("darkmode", false)

        settings_btn_darkmode.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPref.edit().putBoolean("darkmode", true).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPref.edit().putBoolean("darkmode", false).apply()
            }
        }

        //Language
        val languages = listOf(getString(R.string.select), "Español", "English")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)

        binding.settingsSpinnerLanguage.adapter = adapter

        binding.settingsSpinnerLanguage.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    1 -> {
                        setLanguage(this@Settings, "es")
                        sharedPref.edit().putString("language","es").apply()
                        startActivity(Intent(this@Settings, Splashscreen::class.java))
                        finishAffinity()
                    }
                    2 -> {
                        setLanguage(this@Settings, "en")
                        sharedPref.edit().putString("language","en").apply()
                        startActivity(Intent(this@Settings, Splashscreen::class.java))
                        finishAffinity()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setLanguage(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

}