package com.gabrifermar.proyectodam.ui.user

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.ContentValues.TAG
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
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.Usermain
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_usermain, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        //declare variable
        val db = Firebase.firestore
        auth = Firebase.auth


        //call metar api
        (activity as Usermain).loadmetar("LEVS")

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


            }


        //progress bar animation
        //TODO: cuando tenga variables con progreso añadirlas
        ObjectAnimator.ofInt(user_progress_subject, "progress", 75)
            .apply {
                duration = 1500
                start()
                addUpdateListener { updatedAnimation ->
                    user_txt_subjectsprogress.text =
                        getString(R.string.progress, updatedAnimation.animatedValue.toString())
                }
            }


        ObjectAnimator.ofInt(user_progress_flights, "progress", 50)
            .apply {
                duration = 1500
                start()
                addUpdateListener { updatedAnimation ->
                    user_txt_flightsprogress.text =
                        getString(R.string.progress, updatedAnimation.animatedValue.toString())
                }
            }






        user_cv_subjects.setOnClickListener {
            Toast.makeText(activity, "asignaturas", Toast.LENGTH_SHORT).show()
        }

        user_cv_flights.setOnClickListener {
            Toast.makeText(activity, "Vuelos", Toast.LENGTH_SHORT).show()
        }
    }


}