package com.example.locationtaskreminder.broadcast

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.locationtaskreminder.MainActivity
import com.example.locationtaskreminder.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Get the geofence transition type
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val transitionType = geofencingEvent?.geofenceTransition

        // Handle the geofence transition type
        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                // User entered the geofence
                val geofence = geofencingEvent.triggeringGeofences?.get(0)
                val message = "You have entered the geofence ${geofence?.requestId}"
                showNotification(context, message)
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                // User exited the geofence
                val geofence = geofencingEvent.triggeringGeofences?.get(0)
                val message = "You have exited the geofence ${geofence?.requestId}"
                showNotification(context, message)
            }
            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                // User is dwelling inside the geofence
                val geofence = geofencingEvent.triggeringGeofences?.get(0)
                val message = "You are dwelling inside the geofence ${geofence?.requestId}"
                showNotification(context, message)
            }
        }
    }


    private fun showNotification(context: Context, message: String) {
        // Create an Intent for the notification that opens the app's MainActivity
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // Create a notification builder with the message and pending intent
        val notificationBuilder = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Geofence Alert!")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Get the NotificationManagerCompat and show the notification
        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

}
