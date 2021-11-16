package com.gabrifermar.proyectodam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrifermar.proyectodam.databinding.ActivityC172TestBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class C172Test : AppCompatActivity() {

    private lateinit var binding: ActivityC172TestBinding
    private lateinit var adapter: FlightTestAdapter
    private var statement = mutableListOf<String>()
    private var ans1 = mutableListOf<String>()
    private var ans2 = mutableListOf<String>()
    private var ans3 = mutableListOf<String>()
    private var ans4 = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityC172TestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //recycler
        initRecycler()

        //variables

        var min = 4
        var sec = 59

        //counter
        val timer = object : CountDownTimer(300000, 1000) {
            override fun onTick(p0: Long) {

                binding.c172testPbTimer.progress= p0.toInt()

                binding.c172testTvCounter.text = getString(
                    R.string.counter,
                    min.toString().padStart(2, '0'),
                    sec.toString().padStart(2, '0')
                )

                if (sec == 0) {
                    sec = 59
                    min--
                } else {
                    sec--
                }
            }

            override fun onFinish() {
                binding.c172testTvCounter.text =
                    getString(R.string.counter, 0.toString(), 0.toString())
                timeEnd()
            }
        }

        //start test
        loadTest()

        //start timer
        timer.start()

        //listeners
        binding.c172testBtnSubmit.setOnClickListener {
            confirmSubmit()
        }
    }

    private fun loadTest() {
        //variables
        val sharedPreferences = this.getSharedPreferences("C172Test", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        var random: List<Int>
        val db = Firebase.firestore
        try {
            db.collection("C172Test").get().addOnSuccessListener { document ->

                //background task
                CoroutineScope(Dispatchers.IO).launch {

                    //get random numbers
                    do {
                        random =
                            (1..5).map { (1..document.size()).random() }
                    } while (random.distinct().count() != random.size)


                    val questions: HashSet<String> = HashSet()

                    random.forEach {
                        questions.add(it.toString())
                    }

                    sharedPreferences.edit().putStringSet("questions", questions).apply()

                    for (element in random) {
                        db.collection("C172Test").document(element.toString()).get()
                            .addOnSuccessListener {
                                statement.add(it.getString("statement")!!)
                                ans1.add(it.getString("ans1")!!)
                                ans2.add(it.getString("ans2")!!)
                                ans3.add(it.getString("ans3")!!)
                                ans4.add(it.getString("ans4")!!)

                                adapter.notifyDataSetChanged()
                            }
                        delay(100)
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun timeEnd() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.timesup)
            .setMessage(R.string.timesupdialog)
            .setPositiveButton(R.string.submit) { _, _ ->
                checkTestResults()
            }
            .setCancelable(false)
            .create()

        dialog.show()
    }

    private fun confirmSubmit() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.endtest)
            .setMessage(R.string.testdialog)
            .setNegativeButton(R.string.cancel) { view, _ ->
                view.dismiss()
            }
            .setPositiveButton(R.string.submit) { _, _ ->
                checkTestResults()
            }
            .setCancelable(false)
            .create()

        dialog.show()

    }

    private fun checkTestResults() {
        //variables
        val sharedPreferences = this.getSharedPreferences("C172Test", Context.MODE_PRIVATE)
        val index = sharedPreferences.getStringSet("questions", HashSet())
        val db = Firebase.firestore
        val auth = Firebase.auth
        var correctcount = 0
        var incorrectcount = 0
        index?.forEach {
            db.collection("C172Test").document(it).get().addOnSuccessListener { document ->
                val question = document.getString("statement")
                val answered = sharedPreferences.getString(question, "")
                val correct = document.getString("correct")
                if (answered == correct) {
                    correctcount++
                } else {
                    incorrectcount++
                }
                val data = hashMapOf(
                    "C172grade" to (correctcount * 10) / index.size
                )
                db.collection("users").document(auth.currentUser!!.uid)
                    .set(data, SetOptions.merge())
                val sharedPref=getSharedPreferences("user",Context.MODE_PRIVATE)
                sharedPref.edit().putInt("C172grade",((correctcount * 10) / index.size)).apply()
            }
        }

        val intent=Intent(this,Usermain::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun initRecycler() {
        binding.c172RvQuestions.layoutManager = LinearLayoutManager(this)
        adapter = FlightTestAdapter(this, statement, ans1, ans2, ans3, ans4)
        binding.c172RvQuestions.adapter = adapter
    }

    override fun onBackPressed() {
        confirmSubmit()
    }
}