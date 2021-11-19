package com.gabrifermar.proyectodam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gabrifermar.proyectodam.databinding.ActivityNewUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.Exception
import java.lang.IllegalArgumentException

class NewUser : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityNewUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Declare variable and start them
        auth = Firebase.auth
        val db = Firebase.firestore

        binding.btncreateuser.setOnClickListener {

            //Try catch for sign up errors
            try {
                auth.createUserWithEmailAndPassword(
                    binding.txtnewuser.text.toString() + "@hola.com",
                    binding.txtnewpass.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            //Write user config on db if sign up ok
                            val user = hashMapOf(
                                "username" to binding.txtnewuser.text.toString(),
                                "subjects" to binding.cbnewuser1.isChecked,
                                "C172" to binding.cbnewuser2.isChecked,
                                "P28R" to binding.cbnewuser3.isChecked,
                                "P06T" to binding.cbnewuser4.isChecked
                            )

                            db.collection("users").document(auth.currentUser!!.uid)
                                .set(user, SetOptions.merge())

                            when{ binding.cbnewuser1.isChecked -> FirebaseMessaging.getInstance().subscribeToTopic("subjects")}
                            when{ binding.cbnewuser2.isChecked -> FirebaseMessaging.getInstance().subscribeToTopic("C172")}
                            when{ binding.cbnewuser3.isChecked -> FirebaseMessaging.getInstance().subscribeToTopic("P28R")}
                            when{ binding.cbnewuser4.isChecked -> FirebaseMessaging.getInstance().subscribeToTopic("P06T")}

                            Toast.makeText(this,getString(R.string.usercreated),Toast.LENGTH_SHORT).show()

                            finish()
                        } else {
                            //pass less than 6 characters
                            Toast.makeText(this, R.string.passlength, Toast.LENGTH_LONG).show()
                        }
                    }
                auth.signOut()
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, R.string.fillfields, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, R.string.errornewuser, Toast.LENGTH_SHORT).show()
            }

        }
    }


}