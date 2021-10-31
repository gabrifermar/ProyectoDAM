package com.gabrifermar.proyectodam.ui.tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToolsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
    }
    val text: LiveData<String> = _text
}