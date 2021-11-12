package com.gabrifermar.proyectodam.ui.user

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys.AES256_GCM_SPEC
import androidx.security.crypto.MasterKeys.getOrCreate
import com.gabrifermar.proyectodam.FlightMenu
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.Usermain
import com.gabrifermar.proyectodam.databinding.FragmentUsermainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_usermain.*

class UserFragment : Fragment() {

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var flightpb: ObjectAnimator
    private lateinit var subjectpb:ObjectAnimator
    private var _binding: FragmentUsermainBinding?=null

    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentUsermainBinding.inflate(inflater,container,false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_usermain, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        //declare variable
        auth = Firebase.auth
        val sharedPref= (activity as Usermain).getSharedPreferences("user",Context.MODE_PRIVATE)


        //listeners
        binding.userCvFlights.setOnClickListener {
            startActivity(Intent(activity,FlightMenu::class.java))
        }

        //write welcome msg
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            encrypted()
        }else{
            nonencrypted()
        }


        //progress bar animation
        //TODO: cuando tenga variables con progreso añadirlas
        subjectpb=ObjectAnimator.ofInt(user_progress_subject, "progress", 75)
            .apply {
                duration = 1500
                start()
                addUpdateListener { updatedAnimation ->
                    user_txt_subjectsprogress.text =
                        (activity as Usermain).getString(R.string.progress, updatedAnimation.animatedValue.toString())
                }
            }


        flightpb=ObjectAnimator.ofInt(user_progress_flights, "progress", 50)
            .apply {
                duration = 1500
                start()
                addUpdateListener { updatedAnimation ->
                    user_txt_flightsprogress.text =
                        (activity as Usermain).getString(R.string.progress, updatedAnimation.animatedValue.toString())
                }
            }


        //write metar
        Log.d("metar2", sharedPref.getString("metar", "no hay").toString())
        binding.userTxtMetar.text=sharedPref.getString("metar","no hay").toString()

    }

    //non encrypted sharedpref for SDK<23
    private fun nonencrypted() {
        val sharedPref= (activity as Usermain).getSharedPreferences("user",Context.MODE_PRIVATE)
        binding.userTxtUsername.text= (activity as Usermain).getString(R.string.welcome, sharedPref.getString("username",""))
    }

    //encrypted sharedpref
    @SuppressLint("NewApi")
    private fun encrypted() {
        val masterKey = getOrCreate(AES256_GCM_SPEC)
        val encryptedSharedPreferences = EncryptedSharedPreferences.create(
            "user_encrypted",
            masterKey,
            activity as Usermain,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        binding.userTxtUsername.text=(activity as Usermain).getString(R.string.welcome,encryptedSharedPreferences.getString("username",""))
    }

    override fun onPause() {
        flightpb.end()
        subjectpb.end()
        super.onPause()
    }

    override fun onStop() {
        flightpb.end()
        subjectpb.end()
        super.onStop()
    }

    override fun onResume() {
        flightpb.start()
        subjectpb.start()
        super.onResume()
    }

}