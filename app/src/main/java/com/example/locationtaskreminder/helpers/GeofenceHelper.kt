package com.example.locationtaskreminder.helpers

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.locationtaskreminder.broadcast.GeofenceBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class GeofenceHelper(private val context: Context) {

    private val geofencingClient: GeofencingClient by lazy {
        LocationServices.getGeofencingClient(context)
    }

    fun createGeofence(location: LatLng, requestId: String) {
        // Define geofence
        val geofence = Geofence.Builder()
            .setCircularRegion(
                location.latitude,
                location.longitude,
                1000f // radius in meters
            )
            .setRequestId(requestId)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()

        // Define geofencing request
        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        // Register geofence
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
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
        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent())
            .addOnSuccessListener {
                Log.d(TAG, "Geofence created: $geofence")
            }
            .addOnFailureListener { e ->
                Log.i(TAG, "Geofence location: ${location.latitude} && ${location.longitude}")
                Log.e(TAG, "Geofence creation failed: ${e.message.toString()}")
            }
    }

    fun removeGeofence(requestId: String) {
        // Remove geofence
        geofencingClient.removeGeofences(listOf(requestId))
            .addOnSuccessListener {
                Log.d(TAG, "Geofence removed: $requestId")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Geofence removal failed: ${e.message}")
            }
    }

    private fun geofencePendingIntent(): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE

        )
    }

    companion object {
        private const val TAG = "GeofenceHelper"
    }
}
