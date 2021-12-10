package com.gabrifermar.proyectodam.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class FlightMenuViewModel : ViewModel() {

    private val _progressc172 = MutableLiveData<Int>()
    val progressc172 = _progressc172

    fun c172progress() {
        val db = Firebase.firestore
        val auth = Firebase.auth.currentUser
        viewModelScope.launch {
            db.collection("users").document(auth!!.uid).get().addOnSuccessListener {
                _progressc172.value = it.getDouble("C172grade")?.times(10)?.toInt() ?: 0
            }
        }
    }

}