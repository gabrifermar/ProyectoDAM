package com.gabrifermar.proyectodam.ui.user

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.sip.SipSession
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.animation.addPauseListener
import com.gabrifermar.proyectodam.FlightMenu
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.Usermain
import com.gabrifermar.proyectodam.databinding.FragmentUsermainBinding
import com.google.common.base.MoreObjects
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
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
        val db = Firebase.firestore
        auth = Firebase.auth


        //listeners
        binding.userCvFlights.setOnClickListener {
            startActivity(Intent(activity,FlightMenu::class.java))
        }



        //write welcome msg
        val sharedPref= (activity as Usermain).getSharedPreferences("user",Context.MODE_PRIVATE)
        user_txt_username.text= (activity as Usermain).getString(R.string.welcome, sharedPref.getString("username",""))



        /*
        //get inside Firestore database
        db.collection("users").document(auth.currentUser!!.uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    //TODO: posible gestion de bienvenida en background o pantalla de carga hasta que carge todo
                    //setup welcome msg
                    user_txt_username.text =
                        getString(R.string.welcome, document.getString("username"))
                    user_txt_username.visibility = View.VISIBLE

                    //check user settings
                    if (document.getBoolean("subjects") == true) {
                        //check fields
                    } else {
                        //false
                    }


                    Log.d(TAG, document.data.toString())
                    //C172=true, subjects=true, P28R=true, P06T=true, username=hola
                }


            }*/


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
        user_txt_metar.text=sharedPref.getString("metar","no hay").toString()

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