package com.gabrifermar.proyectodam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_new_user.*
import java.lang.Exception
import java.lang.IllegalArgumentException

class NewUser : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        //Declare variable and start them
        auth = Firebase.auth
        val db = Firebase.firestore

        btncreateuser.setOnClickListener {

            Log.d("hola", cbnewuser1.isChecked.toString())
            Log.d("hola", cbnewuser2.isChecked.toString())
            Log.d("hola", cbnewuser3.isChecked.toString())
            Log.d("hola", cbnewuser4.isChecked.toString())

            //Try catch for sign up errors
            try {
                auth.createUserWithEmailAndPassword(
                    txtnewuser.text.toString() + "@hola.com",
                    txtnewpass.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            //Write user config on db if sign up ok
                            val user = hashMapOf(
                                "username" to txtnewuser.text.toString(),
                                "subjects" to cbnewuser1.isChecked,
                                "C172" to cbnewuser2.isChecked,
                                "P28R" to cbnewuser3.isChecked,
                                "P06T" to cbnewuser4.isChecked
                            )


                            db.collection("users").document(auth.currentUser!!.uid)
                                .set(user, SetOptions.merge())

                            startActivity(Intent(this, Admin::class.java))
                            finish()
                        } else {
                            //pass less than 6 characters
                            Toast.makeText(this, "error", Toast.LENGTH_LONG).show()
                        }
                    }
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, R.string.fillfields, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, R.string.errornewuser, Toast.LENGTH_SHORT).show()
            }

        }
    }


}