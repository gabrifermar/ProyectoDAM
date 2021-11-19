package com.gabrifermar.proyectodam.ui.user

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_usermain.*

class UserFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentUsermainBinding? = null
    private lateinit var flightpb: ObjectAnimator
    private lateinit var subjectpb: ObjectAnimator

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsermainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        //declare variable
        auth = Firebase.auth
        val db = Firebase.firestore

        subjectpb = ObjectAnimator.ofInt(user_progress_subject, "progress", 75).apply {
            duration = 1500
            start()
            addUpdateListener { updatedAnimation ->
                user_txt_subjectsprogress.text =
                    (activity as Usermain).getString(
                        R.string.progress,
                        updatedAnimation.animatedValue.toString()
                    )
            }
        }


        flightpb= ObjectAnimator.ofInt(user_progress_flights, "progress", 50).apply {
            duration = 1500
            start()
            addUpdateListener { updatedAnimation ->
                user_txt_flightsprogress.text =
                    (activity as Usermain).getString(
                        R.string.progress,
                        updatedAnimation.animatedValue.toString()
                    )
            }
        }

        //listeners
        binding.userCvFlights.setOnClickListener {
            startActivity(Intent(activity, FlightMenu::class.java))
        }

        //write welcome msg
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.userTxtUsername.text = (activity as Usermain).getString(
                R.string.welcome,
                encrypted().getString("username", "")
            )
        } else {
            binding.userTxtUsername.text = (activity as Usermain).getString(
                R.string.welcome,
                nonencrypted().getString("username", "")
            )
        }

        //write metar
        binding.userTxtMetar.text =
            nonencrypted().getString("metar", getString(R.string.metarerror)).toString()

        //get token id for cloud messaging
        FirebaseMessaging.getInstance().token.addOnCompleteListener { it ->
            it.result.let {
                val data = hashMapOf(
                    "token" to it
                )

                db.collection("users").document(auth.currentUser!!.uid)
                    .set(data, SetOptions.merge()).addOnFailureListener {
                        Toast.makeText(activity, "no", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    //non encrypted sharedpref for SDK<23
    private fun nonencrypted(): SharedPreferences {
        return (activity as Usermain).getSharedPreferences("user", Context.MODE_PRIVATE)
    }

    //encrypted sharedpref
    @SuppressLint("NewApi")
    private fun encrypted(): SharedPreferences {
        val masterKey = getOrCreate(AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "user_encrypted",
            masterKey,
            activity as Usermain,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun onStop() {
        super.onStop()
        subjectpb.end()
        flightpb.end()
    }

    override fun onPause() {
        subjectpb.end()
        flightpb.end()
        super.onPause()
    }

}