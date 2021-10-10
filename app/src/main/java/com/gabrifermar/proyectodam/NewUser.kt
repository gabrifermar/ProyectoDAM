package com.gabrifermar.proyectodam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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
                                "subjects" to rbnewuser1.isChecked,
                                "C172" to rbnewuser2.isChecked,
                                "P28R" to rbnewuser3.isChecked,
                                "P06T" to rbnewuser4.isChecked
                            )

                            db.collection("users").document(auth.currentUser!!.uid)
                                .set(user)

                            startActivity(Intent(this, Home::class.java))
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