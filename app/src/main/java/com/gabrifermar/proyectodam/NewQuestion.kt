package com.gabrifermar.proyectodam

import android.R
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.gabrifermar.proyectodam.databinding.ActivityNewQuestionBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class NewQuestion : AppCompatActivity() {

    private lateinit var binding: ActivityNewQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup spinner
        setupSpinner()

        //listeners
        startListeners()


    }

    private fun startListeners() {
        binding.adminNewquestionBtnAdd.setOnClickListener {
            try {
                addQuestion()
            } catch (e: Exception) {

            }
        }
    }

    private fun addQuestion() {
        val db = Firebase.firestore
        val auth = Firebase.auth
        val collection = "${
            binding.adminNewquestionSpinner.selectedItem.toString().replace("\\s".toRegex(), "")
        }Test"

        //TODO:pte implementar opcion correcta para agregar a firestore
        val question = hashMapOf(
            "statement" to binding.adminNewquestionEtStatement.text.toString(),
            "ans1" to binding.adminNewquestionEtAns1.toString(),
            "ans2" to binding.adminNewquestionEtAns2.toString(),
            "ans3" to binding.adminNewquestionEtAns3.toString(),
            "ans4" to binding.adminNewquestionEtAns4.toString(),
            "correct" to binding.adminNewquestionRgCorrect.checkedRadioButtonId
        )
        auth.signInWithEmailAndPassword("admin@hola.com", "123456")
        db.collection(collection).get().addOnSuccessListener { document ->
            db.collection(collection).document((document.size() + 1).toString()).set(
                question,
                SetOptions.merge()
            )
            auth.signOut()
        }
    }

    private fun setupSpinner() {
        val options =
            listOf(
                getString(com.gabrifermar.proyectodam.R.string.select),
                getString(com.gabrifermar.proyectodam.R.string.C172),
                getString(com.gabrifermar.proyectodam.R.string.P28R),
                getString(com.gabrifermar.proyectodam.R.string.P06T),
                getString(com.gabrifermar.proyectodam.R.string.airlaw),
                getString(com.gabrifermar.proyectodam.R.string.airframe),
                getString(com.gabrifermar.proyectodam.R.string.instrumentation),
                getString(com.gabrifermar.proyectodam.R.string.massbalance),
                getString(com.gabrifermar.proyectodam.R.string.performance),
                getString(com.gabrifermar.proyectodam.R.string.planning),
                getString(com.gabrifermar.proyectodam.R.string.human),
                getString(com.gabrifermar.proyectodam.R.string.meteo),
                getString(com.gabrifermar.proyectodam.R.string.nav),
                getString(com.gabrifermar.proyectodam.R.string.radio),
                getString(com.gabrifermar.proyectodam.R.string.pop),
                getString(com.gabrifermar.proyectodam.R.string.principles),
                getString(com.gabrifermar.proyectodam.R.string.comms),
            )

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, options)

        binding.adminNewquestionSpinner.adapter = adapter
    }
}