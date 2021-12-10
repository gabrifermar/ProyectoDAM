package com.gabrifermar.proyectodam.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Environment.*
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.databinding.ActivityC172Binding
import com.google.firebase.ktx.Firebase
import java.io.File
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore

class C172 : AppCompatActivity() {

    /**
     * @param:builder
     * @param:binding
     */
    private lateinit var binding: ActivityC172Binding
    private lateinit var builder: Notification.Builder
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityC172Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //variables
        val db = Firebase.firestore
        val auth = Firebase.auth
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //backarrow
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //check for grade
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
            if (it.getDouble("C172grade") != null) {
                binding.c172BtnTest.text =
                    this.getString(R.string.grade, it.getDouble("C172grade")!!.toInt().toString())
                binding.c172BtnTest.isClickable = false
            }
        }

        //listeners
        binding.c172BtnPoh.setOnClickListener {
            //check for permission
            if (checkPermission() && isInternetAvailable()) {
                checkFile()
            }
        }

        binding.c172BtnTest.setOnClickListener {
            startActivity(Intent(this, C172Test::class.java))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun notification() {

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(DownloadManager.ACTION_VIEW_DOWNLOADS),
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        notificationChannel = NotificationChannel(
            "Open download directory",
            "Download",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)

        builder = Notification.Builder(this, "Open download directory")
            .setSmallIcon(R.drawable.cloudicon)
            .setContentTitle("Open download directory")
            .setContentText("Click to open download directory")
            .setContentIntent(pendingIntent)


        notificationManager.notify(1234, builder.build())

    }

    private fun checkFile() {
        val file =
            File("${getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)}/Cessna-172N-POH.pdf")

        if (file.exists()) {
            Toast.makeText(this, R.string.filealreadydownloaded, Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification()
            }
        } else {
            download()
        }
    }

    private fun download() {
        val dn: DownloadManager =
            getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val uri =
            Uri.parse("https://firebasestorage.googleapis.com/v0/b/proyectoaep-d6bc6.appspot.com/o/POH%2FCessna-172N-POH.pdf?alt=media&token=e7972c76-0b08-4b74-8794-9fc84222b11f")
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

        //start download
        dn.enqueue(request)

        //check when dowload finish to show notification
        val broadcastReceiver = object : BroadcastReceiver() {
            @SuppressLint("Range")
            override fun onReceive(p0: Context?, p1: Intent?) {
                //File downloaded
            }
        }

        registerReceiver(broadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun isInternetAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var result = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nc = cm.activeNetwork ?: return false
            val active = cm.getNetworkCapabilities(nc)
            result = when {
                active!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                active.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                active.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            cm.run {
                cm.activeNetworkInfo?.run {
                    if (type == TYPE_WIFI) {
                        result = true
                    } else if (type == TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}