package com.gabrifermar.proyectodam.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys.*
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.databinding.ActivityFlightMenuBinding
import com.gabrifermar.proyectodam.viewmodel.FlightMenuViewModel

class FlightMenu : AppCompatActivity() {

    private lateinit var binding: ActivityFlightMenuBinding
    private lateinit var viewModel: FlightMenuViewModel
    private lateinit var c172pb: ObjectAnimator
    private lateinit var p28rpb: ObjectAnimator
    private lateinit var p06tpb: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlightMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //variables
        viewModel = ViewModelProvider(this)[FlightMenuViewModel::class.java]
        c172pb = ObjectAnimator.ofInt(binding.flightmenuPbC172, "progress", 0)
        p28rpb = ObjectAnimator.ofInt(binding.flightmenuPbP28R, "progress", 0)
        p06tpb = ObjectAnimator.ofInt(binding.flightmenuPbP06T, "progress", 0)

        //setupviewmodel
        setupViewModel()

        //backarrow
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //read content from sharedpref to setup locked content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            encrypted()
        } else {
            nonencrypted()
        }

        //C172 progress
        //c172progress = sharedPref.getInt("C172grade", 0)

        //listeners
        binding.flightmenuCvC172.setOnClickListener {
            if (binding.flightmenuIvLockC172.visibility == View.VISIBLE) {
                Toast.makeText(this, getString(R.string.lockedcontent), Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, C172::class.java))
            }
        }

        binding.flightmenuCvP28R.setOnClickListener {
            if (binding.flightmenuIvLockP28R.visibility == View.VISIBLE) {
                Toast.makeText(this, getString(R.string.lockedcontent), Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, P28R::class.java))
            }
        }

        binding.flightmenuCvP06T.setOnClickListener {
            if (binding.flightmenuIvLockP06T.visibility == View.VISIBLE) {
                Toast.makeText(this, getString(R.string.lockedcontent), Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, P06T::class.java))
            }
        }

        //Progress bar
        p28rpb = ObjectAnimator.ofInt(binding.flightmenuPbP28R, "progress", 50)
            .apply {
                duration = 1500
                start()
                addUpdateListener { updatedAnimation ->
                    binding.flightmenuTxtP28Rprogress.text =
                        this@FlightMenu.getString(
                            R.string.progress,
                            updatedAnimation.animatedValue.toString()
                        )
                }
            }

        p06tpb = ObjectAnimator.ofInt(binding.flightmenuPbP06T, "progress", 50)
            .apply {
                duration = 1500
                start()
                addUpdateListener { updatedAnimation ->
                    binding.flightmenuTxtP06Tprogress.text =
                        getString(R.string.progress, updatedAnimation.animatedValue.toString())
                }
            }
    }

    private fun setupViewModel() {
        viewModel.c172progress()
        viewModel.progressc172.observe(this@FlightMenu, {
            c172pb = ObjectAnimator.ofInt(binding.flightmenuPbC172, "progress", it ?: 0)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.flightmenuTxtC172progress.text = this@FlightMenu.getString(
                            R.string.progress,
                            updatedAnimation.animatedValue.toString()
                        )
                    }
                }
        })
    }

    @SuppressLint("NewApi")
    private fun encrypted() {
        val masterKey = getOrCreate(AES256_GCM_SPEC)
        val encryptedSharedPreferences = EncryptedSharedPreferences.create(
            "user_encrypted",
            masterKey,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        if (!encryptedSharedPreferences.getBoolean("C172", false)) {
            binding.flightmenuTxtC172progress.visibility = View.INVISIBLE
            binding.flightmenuIvLockC172.visibility = View.VISIBLE
            binding.flightmenuPbC172.visibility = View.INVISIBLE
        }

        if (!encryptedSharedPreferences.getBoolean("P28R", false)) {
            binding.flightmenuTxtP28Rprogress.visibility = View.INVISIBLE
            binding.flightmenuIvLockP28R.visibility = View.VISIBLE
            binding.flightmenuPbP28R.visibility = View.INVISIBLE
        }

        if (!encryptedSharedPreferences.getBoolean("P06T", false)) {
            binding.flightmenuTxtP06Tprogress.visibility = View.INVISIBLE
            binding.flightmenuIvLockP06T.visibility = View.VISIBLE
            binding.flightmenuPbP06T.visibility = View.INVISIBLE
        }

    }

    //retrieve data from SDK < 23, non encrypted
    private fun nonencrypted() {

        val sharedPref = this.getSharedPreferences("user", Context.MODE_PRIVATE)

        if (!sharedPref.getBoolean("C172", false)) {
            binding.flightmenuTxtC172progress.visibility = View.INVISIBLE
            binding.flightmenuIvLockC172.visibility = View.VISIBLE
            binding.flightmenuPbC172.visibility = View.INVISIBLE
        }

        if (!sharedPref.getBoolean("P28R", false)) {
            binding.flightmenuTxtP28Rprogress.visibility = View.INVISIBLE
            binding.flightmenuIvLockP28R.visibility = View.VISIBLE
            binding.flightmenuPbP28R.visibility = View.INVISIBLE
        }

        if (!sharedPref.getBoolean("P06T", false)) {
            binding.flightmenuTxtP06Tprogress.visibility = View.INVISIBLE
            binding.flightmenuIvLockP06T.visibility = View.VISIBLE
            binding.flightmenuPbP06T.visibility = View.INVISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        c172pb.end()
        p28rpb.end()
        p06tpb.end()
        super.onPause()
    }

    override fun onStop() {
        c172pb.end()
        p28rpb.end()
        p06tpb.end()
        super.onStop()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}