package com.gabrifermar.proyectodam.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.DEFAULT_CONCURRENCY_PROPERTY_NAME

class SubjectContentViewModel : ViewModel() {

    private var _contentTitle = MutableLiveData<MutableList<String>>()
    var contentTitle: LiveData<List<String>> = _contentTitle as LiveData<List<String>>

    fun getContentTitle(ref: String) {
        val db = Firebase.firestore

        db.collection("subjects/$ref/content").get().addOnSuccessListener { documents ->
            for (document in documents) {
                //Log.e("error", document.id)
                _contentTitle.value?.add(document.id)
            }
        }
    }

}