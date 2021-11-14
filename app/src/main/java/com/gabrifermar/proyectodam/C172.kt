package com.gabrifermar.proyectodam

import android.Manifest
import android.annotation.SuppressLint
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
import android.webkit.DownloadListener
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri

class C172 : AppCompatActivity() {

    /**
     * @param:builder
     * @param:binding
     *
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
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        //listeners
        binding.c172BtnPoh.setOnClickListener {
            //check for permission
            if (checkPermission()) {
                download()
            }
        }

        binding.c172BtnTest.setOnClickListener {
            startActivity(Intent(this, C172Test::class.java))
        }
    }

    private fun notification() {
/*
        val builder = NotificationCompat.Builder(this, "HOLA").apply {
            setContentTitle("Picture Download")
            setContentText("Download in progress")
            setSmallIcon(R.drawable.cloud)
            setPriority(NotificationCompat.PRIORITY_LOW)
        }
        val PROGRESS_MAX = 100
        val PROGRESS_CURRENT = 0
        NotificationManagerCompat.from(this).apply {
            // Issue the initial notification with zero progress
            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
            notify(notificationId, builder.build())

            // Do the job here that tracks the progress.
            // Usually, this should be in a
            // worker thread
            // To show progress, update PROGRESS_CURRENT and update the notification with:
            // builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
            // notificationManager.notify(notificationId, builder.build());

            // When done, update the notification one more time to remove the progress bar
            builder.setContentText("Download complete")
                .setProgress(0, 0, false)
            notify(notificationId, builder.build())
        }
*/


        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                "com.gabrifermar.proyectodam",
                "prueba",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, "com.gabrifermar.proyectodam")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentText("buenas tardes")
                .setContentIntent(pendingIntent)
        } else {

            builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234, builder.build())

    }

    private fun download() {
        val dn: DownloadManager =
            getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val uri =
            Uri.parse("https://firebasestorage.googleapis.com/v0/b/proyectoaep-d6bc6.appspot.com/o/Cessna-172N-POH.pdf?alt=media&token=30bc84a6-ee3c-4e6c-9ce6-0061ed5ddc11")
        val request = DownloadManager.Request(uri)

        val query: DownloadManager.Query = DownloadManager.Query()

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

                /*
                val id= p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,0)

                val query=DownloadManager.Query()

                query.setFilterById(enq)

                val dowloadmanager=getSystemService(DOWNLOAD_SERVICE) as DownloadManager

                val cursor=dowloadmanager.query(query)


                if(cursor.moveToFirst()){
                    val colindex=cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    if(DownloadManager.STATUS_SUCCESSFUL==cursor.getInt(colindex)){
                        val uristring=cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                        val type=cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE))

                        val intent=Intent()
                        intent.action=Intent.ACTION_VIEW
                        intent.setDataAndType(uristring.toUri(),type)
                        intent.flags=Intent.FLAG_GRANT_READ_URI_PERMISSION
                        startActivity(intent)
                    }
                }
*/
                //startActivity(Intent(this@C172,dowloadmanager.openDownloadedFile(id!!)::class.java))

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