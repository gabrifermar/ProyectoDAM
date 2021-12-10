package com.gabrifermar.proyectodam.view.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys.AES256_GCM_SPEC
import androidx.security.crypto.MasterKeys.getOrCreate
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.databinding.FragmentUsermainBinding
import com.gabrifermar.proyectodam.view.FlightMenu
import com.gabrifermar.proyectodam.view.SubjectMenu
import com.gabrifermar.proyectodam.view.Usermain
import com.gabrifermar.proyectodam.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class UserFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var flightpb: ObjectAnimator
    private lateinit var subjectpb: ObjectAnimator
    private val binding get() = _binding!!
    private var _binding: FragmentUsermainBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsermainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        //declare variable
        auth = Firebase.auth
        val db = Firebase.firestore
        flightpb = ObjectAnimator.ofInt(binding.userPbFlights, "progress", 0)
        subjectpb = ObjectAnimator.ofInt(binding.userPbSubject, "progress", 0)

        //setup viewmodel
        setupViewModel()

        //setup content
        setupContent()

        //listeners
        startListeners()

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

    private fun startListeners() {
        binding.userCvFlights.setOnClickListener {
            startActivity(Intent(activity, FlightMenu::class.java))
        }

        binding.userCvSubjects.setOnClickListener {
            startActivity(Intent(activity, SubjectMenu::class.java))
        }
    }

    private fun setupContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!encrypted().getBoolean("subjects", false)) {
                binding.userTxtSubjectsprogress.visibility = View.INVISIBLE
                binding.userPbSubject.visibility = View.INVISIBLE
                binding.userIvLockSubjects.visibility = View.VISIBLE
            }
        } else {
            if (!nonencrypted().getBoolean("subjects", false)) {
                binding.userTxtSubjectsprogress.visibility = View.INVISIBLE
                binding.userPbSubject.visibility = View.INVISIBLE
                binding.userIvLockSubjects.visibility = View.VISIBLE
            }
        }
    }

    private fun setupViewModel() {
        //flightprogress
        viewModel.flightProgress()
        viewModel.flightProgress.observe(activity as Usermain, {
            flightpb = ObjectAnimator.ofInt(binding.userPbFlights, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.userTxtFlightsprogress.text =
                            (activity as Usermain).getString(
                                R.string.progress,
                                updatedAnimation.animatedValue.toString()
                            )
                    }
                }
        })

        //subjectprogress
        viewModel.subjectProgress()
        viewModel.subjectProgress.observe(activity as Usermain, {
            subjectpb = ObjectAnimator.ofInt(binding.userPbSubject, "progress", it)
                .apply {
                    duration = 1500
                    start()
                    addUpdateListener { updatedAnimation ->
                        binding.userTxtSubjectsprogress.text =
                            (activity as Usermain).getString(
                                R.string.progress,
                                updatedAnimation.animatedValue.toString()
                            )
                    }
                }
        })

    }

    //non encrypted sharedpref for SDK<23
    private fun nonencrypted(): SharedPreferences {
        return (activity as Usermain).getSharedPreferences("user", Context.MODE_PRIVATE)
    }

    //encrypted sharedpref
    @RequiresApi(Build.VERSION_CODES.M)
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
