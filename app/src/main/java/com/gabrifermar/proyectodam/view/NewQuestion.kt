package com.gabrifermar.proyectodam.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gabrifermar.proyectodam.R
import android.widget.ArrayAdapter
import android.widget.Toast
import com.gabrifermar.proyectodam.databinding.ActivityNewQuestionBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewQuestion : AppCompatActivity() {

    private lateinit var binding: ActivityNewQuestionBinding
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //variables
        auth.signInWithEmailAndPassword("admin@hola.com", "123456")

        //setup spinner
        setupSpinner()

        //listeners
        startListeners()

    }

    private fun startListeners() {
        binding.adminNewquestionBtnAdd.setOnClickListener {
            addQuestion()
        }
    }

    private fun addQuestion() {
        val db = Firebase.firestore
        var correct: String? = null
        val collection = "${
            binding.adminNewquestionSpinner.selectedItem.toString().replace("\\s".toRegex(), "")
        }Test"

        when (binding.adminNewquestionRgCorrect.checkedRadioButtonId) {
            1 -> correct = binding.adminNewquestionEtAns1.text.toString()
            2 -> correct = binding.adminNewquestionEtAns2.text.toString()
            3 -> correct = binding.adminNewquestionEtAns3.text.toString()
            4 -> correct = binding.adminNewquestionEtAns4.text.toString()
        }

        //check for empty fields
        if (correct.isNullOrEmpty() ||
            binding.adminNewquestionEtAns1.text.isNullOrEmpty() ||
            binding.adminNewquestionEtAns2.text.isNullOrEmpty() ||
            binding.adminNewquestionEtAns3.text.isNullOrEmpty() ||
            binding.adminNewquestionEtAns4.text.isNullOrEmpty()
        ) {
            Toast.makeText(this, R.string.emptyfield, Toast.LENGTH_SHORT).show()
        } else {
            val question = hashMapOf(
                "statement" to binding.adminNewquestionEtStatement.text.toString(),
                "ans1" to binding.adminNewquestionEtAns1.text.toString(),
                "ans2" to binding.adminNewquestionEtAns2.text.toString(),
                "ans3" to binding.adminNewquestionEtAns3.text.toString(),
                "ans4" to binding.adminNewquestionEtAns4.text.toString(),
                "correct" to correct
            )
            db.collection(collection).get().addOnSuccessListener { document ->
                db.collection(collection).document((document.size() + 1).toString()).set(question)
                binding.adminNewquestionEtStatement.text.clear()
                binding.adminNewquestionEtAns1.text.clear()
                binding.adminNewquestionEtAns2.text.clear()
                binding.adminNewquestionEtAns3.text.clear()
                binding.adminNewquestionEtAns4.text.clear()
                currentFocus?.clearFocus()
            }
        }
    }

    private fun setupSpinner() {
        val options =
            listOf(
                getString(R.string.select),
                getString(R.string.C172),
                getString(R.string.P28R),
                getString(R.string.P06T),
                getString(R.string.airlaw),
                getString(R.string.airframe),
                getString(R.string.instrumentation),
                getString(R.string.massbalance),
                getString(R.string.performance),
                getString(R.string.planning),
                getString(R.string.human),
                getString(R.string.meteo),
                getString(R.string.nav),
                getString(R.string.radio),
                getString(R.string.pop),
                getString(R.string.principles),
                getString(R.string.comms),
            )

        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, options)

        binding.adminNewquestionSpinner.adapter = adapter
    }

    override fun onPause() {
        auth.signOut()
        super.onPause()
    }

    override fun onStop() {
        auth.signOut()
        super.onStop()
    }

    override fun onRestart() {
        auth.signInWithEmailAndPassword("admin@hola.com", "123456")
        super.onRestart()
    }

    override fun onResume() {
        auth.signInWithEmailAndPassword("admin@hola.com", "123456")
        super.onResume()
    }

    override fun onBackPressed() {
        auth.signOut()
        super.onBackPressed()
    }
}