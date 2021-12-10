package com.gabrifermar.proyectodam.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import com.gabrifermar.proyectodam.databinding.ActivitySettingsBinding
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*
import android.view.MenuItem
import com.gabrifermar.proyectodam.R


class Settings : AppCompatActivity() {


    private lateinit var binding: ActivitySettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //backarrow
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //setup spinner
        setupSpinner()

        //listeners
        startListeners()

    }

    private fun startListeners() {
        val sharedPref = this.getSharedPreferences("mode", Context.MODE_PRIVATE)

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
                        setLanguage(this@Settings, "en")
                        sharedPref.edit().putString("language", "en").apply()
                        startActivity(Intent(this@Settings, Splashscreen::class.java))
                        finishAffinity()
                    }
                    2 -> {
                        setLanguage(this@Settings, "es")
                        sharedPref.edit().putString("language", "es").apply()
                        startActivity(Intent(this@Settings, Splashscreen::class.java))
                        finishAffinity()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.settingsBtnDarkmode.isChecked = sharedPref.getBoolean("darkmode", false)

        settings_btn_darkmode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPref.edit().putBoolean("darkmode", true).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPref.edit().putBoolean("darkmode", false).apply()
            }
        }
    }

    private fun setupSpinner() {
        val languages =
            listOf(getString(R.string.select), getString(R.string.en), getString(R.string.es))

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)

        binding.settingsSpinnerLanguage.adapter = adapter
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
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