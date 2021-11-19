package com.gabrifermar.proyectodam

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Looper.prepare()

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(baseContext, remoteMessage.notification?.body, Toast.LENGTH_SHORT).show()
        }

        Looper.loop()
    }

}