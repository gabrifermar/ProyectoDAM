package com.gabrifermar.proyectodam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt

class UserViewModel : ViewModel() {

    //variables
    private val db = Firebase.firestore
    private val auth = Firebase.auth.currentUser

    private val _flightProgress = MutableLiveData<Int>()
    var flightProgress: LiveData<Int> = _flightProgress

    private val _subjectProgress = MutableLiveData<Int>()
    val subjectProgress: LiveData<Int> = _subjectProgress

    fun flightProgress() {
        var c172: Double
        var p28r: Double
        var p06t: Double
        db.collection("users").document(auth!!.uid).get().addOnSuccessListener {
            c172 = it.getDouble("C172grade")?.times(10.0) ?: 0.0
            c172 /= 3
            p28r = it.getDouble("P28Rgrade")?.times(10.0) ?: 0.0
            p28r /= 3
            p06t = it.getDouble("P06Tgrade")?.times(10.0) ?: 0.0
            p06t /= 3

            _flightProgress.value = (c172 + p28r + p06t).roundToInt()
        }
    }

    fun subjectProgress() {
        var airlaw: Int
        var airframe: Int
        var comms: Int
        var human: Int
        var instrumentation: Int
        var massbalance: Int
        var meteo: Int
        var nav: Int
        var performance: Int
        var planning: Int
        var pop: Int
        var principles: Int
        var radio: Int
        var total: Int

        db.collection("users").document(auth!!.uid).get().addOnSuccessListener {
            airlaw = it.getDouble("airlawgrade")?.toInt() ?: 0
            airframe = it.getDouble("airframegrade")?.toInt() ?: 0
            comms = it.getDouble("commsgrade")?.toInt() ?: 0
            human = it.getDouble("humangrade")?.toInt() ?: 0
            instrumentation = it.getDouble("instrumentationgrade")?.toInt() ?: 0
            massbalance = it.getDouble("massbalancegrade")?.toInt() ?: 0
            meteo = it.getDouble("meteograde")?.toInt() ?: 0
            nav = it.getDouble("navgrade")?.toInt() ?: 0
            performance = it.getDouble("performancegrade")?.toInt() ?: 0
            planning = it.getDouble("planninggrade")?.toInt() ?: 0
            pop = it.getDouble("popgrade")?.toInt() ?: 0
            principles = it.getDouble("principlesgrade")?.toInt() ?: 0
            radio = it.getDouble("radiograde")?.toInt() ?: 0

            total = airlaw + airframe + comms + human + instrumentation +
                    massbalance + meteo + nav + performance + planning + pop + principles + radio

            total *= 10

            total /= 13

            _subjectProgress.value = total

        }
    }
}