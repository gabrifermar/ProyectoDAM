package com.gabrifermar.proyectodam

import android.content.ContentValues.TAG
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Looper.prepare()

        Handler().post {
            Toast.makeText(baseContext, remoteMessage.notification?.body, Toast.LENGTH_SHORT).show()
        }

        Looper.loop()
    }
}