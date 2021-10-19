package com.gabrifermar.proyectodam.ui.tools

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.Usermain
import com.gabrifermar.proyectodam.databinding.FragmentUserToolsBinding
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.fragment_user_tools.*
import java.util.jar.Manifest

class ToolsFragment : Fragment() {

    private var _binding: FragmentUserToolsBinding? = null
    private lateinit var viewModel: ToolsViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_tools, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ToolsViewModel::class.java)

        user_tools_cv_weather.setOnClickListener {
            Toast.makeText(activity, "weather", Toast.LENGTH_SHORT).show()
        }


        checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, "location", 101)

        /*
        val requestPermission=registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted : Boolean ->
            if (isGranted){
                Log.d("permisos", "ok")
            }else{
                Log.d("permisos","no")
            }
        }*/

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkPermission(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    (activity as Usermain),
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d("permision", "ok")
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission,
                    name,
                    requestCode
                )

                else -> ActivityCompat.requestPermissions(
                    (activity as Usermain),
                    arrayOf(permission),
                    requestCode
                )
            }
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder((activity as Usermain))

        builder.apply {
            setMessage("acepta tonto")
            setTitle("Permision required")
            setPositiveButton("ok") { dialog, which ->
                ActivityCompat.requestPermissions(
                    (activity as Usermain),
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()

    }


}