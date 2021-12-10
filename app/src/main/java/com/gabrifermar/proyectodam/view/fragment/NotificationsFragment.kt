package com.gabrifermar.proyectodam.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.databinding.FragmentNotificationsBinding
import com.gabrifermar.proyectodam.view.Usermain
import com.gabrifermar.proyectodam.viewmodel.NotificationsViewModel

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notificationsViewModel =
            ViewModelProvider(this)[NotificationsViewModel::class.java]

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textNotifications.text = (activity as Usermain).getString(R.string.wip)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}