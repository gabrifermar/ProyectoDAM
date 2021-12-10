package com.gabrifermar.proyectodam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SubjectMenuViewModel : ViewModel() {

    //variables
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private val _airlawProgress = MutableLiveData<Int>()
    val airlawProgress: LiveData<Int> = _airlawProgress

    private val _airframeProgress = MutableLiveData<Int>()
    val airframeProgress: LiveData<Int> = _airframeProgress

    private val _commsProgress = MutableLiveData<Int>()
    val commsProgress: LiveData<Int> = _commsProgress

    private val _humanProgress = MutableLiveData<Int>()
    val humanProgress: LiveData<Int> = _humanProgress

    private val _instrumentationProgress = MutableLiveData<Int>()
    val instrumentationProgress: LiveData<Int> = _instrumentationProgress

    private val _massbalanceProgress = MutableLiveData<Int>()
    val massbalanceProgress: LiveData<Int> = _massbalanceProgress

    private val _meteoProgress = MutableLiveData<Int>()
    val meteoProgress: LiveData<Int> = _meteoProgress

    private val _navProgress = MutableLiveData<Int>()
    val navProgress: LiveData<Int> = _navProgress

    private val _performanceProgress = MutableLiveData<Int>()
    val performanceProgress: LiveData<Int> = _performanceProgress

    private val _planningProgress = MutableLiveData<Int>()
    val planningProgress: LiveData<Int> = _planningProgress

    private val _popProgress = MutableLiveData<Int>()
    val popProgress: LiveData<Int> = _popProgress

    private val _principlesProgress = MutableLiveData<Int>()
    val principlesProgress: LiveData<Int> = _principlesProgress

    private val _radioProgress = MutableLiveData<Int>()
    val radioProgress: LiveData<Int> = _radioProgress

    fun airlawProgress() {
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            _airlawProgress.value=it.getDouble("airlawgrade")?.times(10)?.toInt() ?: 0
        }
    }

    fun airframeProgress() {
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            _airframeProgress.value=it.getDouble("airframegrade")?.times(10)?.toInt() ?: 0
        }
    }

    fun commsProgress(){
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            _commsProgress.value=it.getDouble("commsgrade")?.times(10)?.toInt() ?: 0
        }
    }

    fun humanProgress(){
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            _humanProgress.value=it.getDouble("humangrade")?.times(10)?.toInt() ?: 0
        }
    }

    fun instrumentationProgress(){
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            _instrumentationProgress.value=it.getDouble("instrumentationgrade")?.times(10)?.toInt() ?: 0
        }
    }

    fun massbalanceProgress(){
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            _massbalanceProgress.value=it.getDouble("massbalancegrade")?.times(10)?.toInt() ?: 0
        }
    }

    fun meteoProgress(){
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            _meteoProgress.value=it.getDouble("meteograde")?.times(10)?.toInt() ?: 0
        }
    }

    fun navProgress(){
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            _navProgress.value=it.getDouble("navgrade")?.times(10)?.toInt() ?: 0
        }
    }

    fun performanceProgress(){
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            _performanceProgress.value=it.getDouble("performancegrade")?.times(10)?.toInt() ?: 0
        }
    }

    fun planningProgress(){
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            _planningProgress.value=it.getDouble("planninggrade")?.times(10)?.toInt() ?: 0
        }
    }

    fun popProgress(){
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            _popProgress.value=it.getDouble("popgrade")?.times(10)?.toInt() ?: 0
        }
    }

    fun principlesProgress(){
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            _principlesProgress.value=it.getDouble("principlesgrade")?.times(10)?.toInt() ?: 0
        }
    }

    fun radioProgress(){
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            _radioProgress.value=it.getDouble("radiograde")?.times(10)?.toInt() ?: 0
        }
    }
}