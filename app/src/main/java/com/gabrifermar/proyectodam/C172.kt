package com.gabrifermar.proyectodam

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gabrifermar.proyectodam.databinding.ActivityC172Binding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import android.os.Environment.DIRECTORY_DOWNLOADS

class C172 : AppCompatActivity() {

    private lateinit var binding: ActivityC172Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityC172Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val storage = Firebase.storage
        val ref =
            storage.getReferenceFromUrl("gs://proyectoaep-d6bc6.appspot.com/Cessna-172N-POH.pdf")
        val localfile = File.createTempFile("Cessna-172N-POH", "pdf")


        //listeners
        binding.c172BtnPoh.setOnClickListener {
            //check for permission
            if (checkPermission()) {
                val dn: DownloadManager =
                    getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val uri =
                    Uri.parse("https://firebasestorage.googleapis.com/v0/b/proyectoaep-d6bc6.appspot.com/o/Cessna-172N-POH.pdf?alt=media&token=30bc84a6-ee3c-4e6c-9ce6-0061ed5ddc11")
                val request = DownloadManager.Request(uri)

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalFilesDir(
                    this,
                    DIRECTORY_DOWNLOADS,
                    "Cessna-172N-POH.pdf"
                )
                request.setDestinationInExternalPublicDir(
                    DIRECTORY_DOWNLOADS,
                    "Cessna-172N-POH.pdf"
                )

                dn.enqueue(request)
            }
        }
    }

    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) -> {
                    return true
                }
                else -> ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    103
                )
            }
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            //no permitido
        } else {
            //permitido
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}