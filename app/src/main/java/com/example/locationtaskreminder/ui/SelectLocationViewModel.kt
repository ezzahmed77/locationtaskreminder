package com.example.locationtaskreminder.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices

private const val PERMISSIONS_REQUEST_LOCATION = 1

class SelectLocationViewModel(): ViewModel() {
    private val _reminderLocation = MutableLiveData<Location>()
    val reminderLocation: LiveData<Location> = _reminderLocation

    fun setReminderLocation(location: Location) {
        _reminderLocation.value = location
    }
    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location



     fun getUserLocation(context: Context) {

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request for it
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_LOCATION
            )
        } else {
            // Permission is already granted, get the last known location
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    location?.let {
                        _location.value = location
                    }
                }
        }
    }


}