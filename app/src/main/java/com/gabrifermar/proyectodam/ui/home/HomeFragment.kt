package com.gabrifermar.proyectodam.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gabrifermar.proyectodam.Home
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: cambiar nombre de fragmentos en toolbar
        //(activity as Home).supportActionBar?.title="prueba"


        //listeners

        val gmmIntentUri = Uri.parse("geo:0, 0?q=Edificio AEROTEC, Aeropuerto de Cuatro Vientos, 28054 Madrid")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}