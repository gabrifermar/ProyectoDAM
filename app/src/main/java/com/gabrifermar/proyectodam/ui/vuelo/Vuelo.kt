package com.gabrifermar.proyectodam.ui.vuelo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gabrifermar.proyectodam.R

class Vuelo : Fragment() {

    companion object {
        fun newInstance() = Vuelo()
    }

    private lateinit var viewModel: VueloViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vuelo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VueloViewModel::class.java)
    }

}