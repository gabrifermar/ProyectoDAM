package com.gabrifermar.proyectodam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gabrifermar.proyectodam.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_login.*

class Login : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        //listeners
        binding.btnlogin.setOnClickListener {
            login()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun login() {

        //variable
        auth = Firebase.auth
        val db = Firebase.firestore

        //val gsReference = storage.getReferenceFromUrl("gs://proyectoaep-d6bc6.appspot.com/Certificado.pdf")
        if (binding.username.text.isNotEmpty() || binding.password.text.isNotEmpty()) {

            auth.signInWithEmailAndPassword(
                binding.username.text.toString() + "@hola.com",
                binding.password.text.toString()
            )
                .addOnCompleteListener(activity as Home) { task ->
                    if (task.isSuccessful) {

                        db.collection("users").document(auth.currentUser!!.uid).get()
                            .addOnSuccessListener { document ->
                                if (document != null) {

                                    val sharedPref =
                                        (activity as Home).getSharedPreferences(
                                            "user",
                                            Context.MODE_PRIVATE
                                        )
                                    sharedPref.edit()
                                        .putString("username", document.getString("username"))
                                        .putBoolean("C172", document.getBoolean("C172")!!)
                                        .putBoolean("P28R", document.getBoolean("P28R")!!)
                                        .putBoolean("P06T", document.getBoolean("P06T")!!)
                                        .putBoolean("subjects", document.getBoolean("subjects")!!)
                                        .apply()

                                }
                            }

                        startActivity(Intent(activity, Usermain::class.java))

                        //admin access
                    } else if (binding.username.text.toString() == "admin" && binding.password.text.toString() == "admin") {
                        auth.signInAnonymously()
                        startActivity(Intent(activity, Admin::class.java))

                        //error
                    } else {
                        Toast.makeText(activity, "error", Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            Toast.makeText(activity, getString(R.string.errornewuser), Toast.LENGTH_LONG).show()
        }
    }
}
