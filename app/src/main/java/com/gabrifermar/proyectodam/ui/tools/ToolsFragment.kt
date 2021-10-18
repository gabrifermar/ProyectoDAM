package com.gabrifermar.proyectodam.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gabrifermar.proyectodam.databinding.FragmentUserToolsBinding

class ToolsFragment : Fragment() {

    private lateinit var dashboardViewModel: ToolsViewModel
    private var _binding: FragmentUserToolsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(ToolsViewModel::class.java)

        _binding = FragmentUserToolsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })


        //TODO: empezar tools fragment
        Toast.makeText(activity,"hola",Toast.LENGTH_SHORT).show()

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}