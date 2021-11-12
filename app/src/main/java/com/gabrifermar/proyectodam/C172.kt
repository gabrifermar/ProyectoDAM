package com.gabrifermar.proyectodam

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
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
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class C172 : AppCompatActivity() {

    private lateinit var binding: ActivityC172Binding
    private lateinit var builder: Notification.Builder
    private lateinit var notificationManager : NotificationManager
    private lateinit var notificationChannel : NotificationChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityC172Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //variables
        notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        //listeners
        binding.c172BtnPoh.setOnClickListener {
            //check for permission
            if (checkPermission()) {
                download()
            }
        }
    }

    private fun notification() {

        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel("com.gabrifermar.proyectodam","prueba",NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this,"com.gabrifermar.proyectodam")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentText("buenas tardes")
                .setContentIntent(pendingIntent)
        }else{

            builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234,builder.build())

    }

    private fun download() {
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

        //start download
        dn.enqueue(request)

        //check when dowload finish to show notification
        val broadcastReceiver=object :BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                notification()
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

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