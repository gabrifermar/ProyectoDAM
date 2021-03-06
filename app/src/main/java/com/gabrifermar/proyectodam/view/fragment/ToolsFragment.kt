package com.gabrifermar.proyectodam.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gabrifermar.proyectodam.databinding.FragmentUserToolsBinding
import com.gabrifermar.proyectodam.view.Charts
import com.gabrifermar.proyectodam.view.FlightPlanner
import com.gabrifermar.proyectodam.view.WeatherReports
import com.gabrifermar.proyectodam.viewmodel.ToolsViewModel

class ToolsFragment : Fragment() {

    private var _binding: FragmentUserToolsBinding? = null
    private lateinit var viewModel: ToolsViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserToolsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ToolsViewModel::class.java]

        //listeners
        binding.userToolsCvWeather.setOnClickListener {
            startActivity(Intent(activity, WeatherReports::class.java))
        }

        binding.userToolsCvCharts.setOnClickListener {
            startActivity(Intent(activity, Charts::class.java))
        }

        binding.userToolsCvFlightplanner.setOnClickListener {
            startActivity(Intent(activity, FlightPlanner::class.java))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}