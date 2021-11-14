package com.gabrifermar.proyectodam

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys.*
import com.gabrifermar.proyectodam.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Login : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private lateinit var auth: FirebaseAuth
    private val metarlist = mutableListOf<String>()


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        //listeners
        binding.btnlogin.setOnClickListener {
            binding.username.clearFocus()
            binding.password.clearFocus()
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

                                    //check system version to encrypt data, if SDK below 23, it wont encrypt it
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        //create masterkey
                                        val masterKey =
                                            getOrCreate(AES256_GCM_SPEC)
                                        val encryptedSharedPreferences =
                                            EncryptedSharedPreferences.create(
                                                "user_encrypted",
                                                masterKey,
                                                activity as Home,
                                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                                            )
                                        encryptedSharedPreferences.edit()
                                            .putString("username", document.getString("username"))
                                            .putBoolean("C172", document.getBoolean("C172")!!)
                                            .putBoolean("P28R", document.getBoolean("P28R")!!)
                                            .putBoolean("P06T", document.getBoolean("P06T")!!)
                                            .putBoolean(
                                                "subjects",
                                                document.getBoolean("subjects")!!
                                            )
                                            .apply()
                                    } else {
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
                                            .putBoolean(
                                                "subjects",
                                                document.getBoolean("subjects")!!
                                            )
                                            .apply()
                                    }
                                }
                            }

                        loadmetar("LEVS")

                        //show loading progress bar
                        binding.btnlogin.visibility=View.INVISIBLE
                        binding.customPbLoading.visibility=View.VISIBLE

                        //wait time to set shared pref and be able to retrieve it at userfragment start
                        Handler().postDelayed({
                            startActivity(Intent(activity, Usermain::class.java))
                            binding.btnlogin.visibility=View.VISIBLE
                            binding.customPbLoading.visibility=View.INVISIBLE
                        }, 2000)


                        //admin access
                    } else if (binding.username.text.toString() == "admin" && binding.password.text.toString() == "admin") {
                        auth.signInAnonymously()
                        startActivity(Intent(activity, Admin::class.java))

                        //error
                    } else {
                        Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            Toast.makeText(activity, getString(R.string.errornewuser), Toast.LENGTH_LONG).show()
        }
    }

    private fun getmetarcall(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.checkwx.com/metar/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    internal fun loadmetar(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getmetarcall().create(API::class.java)
                .getMetar("$query/?x-api-key=d49660ce845e4f3db1fc469256")
            val levs = call.body()
            (activity as Home).runOnUiThread {
                if (call.isSuccessful) {
                    val metars = levs?.data ?: emptyList()
                    metarlist.clear()
                    metarlist.addAll(metars)
                    val sharedPref =
                        (activity as Home).getSharedPreferences("user", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("metar", metarlist[0]).apply()
                }
            }
        }
    }

}
