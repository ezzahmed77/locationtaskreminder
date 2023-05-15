package com.example.locationtaskreminder.ui

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.example.locationtaskreminder.data.model.Reminder
import com.example.locationtaskreminder.data.repository.LocationTaskRepository
import com.example.locationtaskreminder.helpers.GeofenceHelper
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class CreateTaskViewModel(
    private val context: Context,
    private val locationTaskRepository: LocationTaskRepository
) : ViewModel() {
    // TODO: Implement the ViewModel

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    private val geofenceHelper = GeofenceHelper(context)

    // Navigation
    var navigationState = MutableStateFlow(false)

    fun setTitle(title: String) {
        _title.value = title
    }

    fun setDescription(description: String) {
        _description.value = description
    }

    fun setLocation(location: Location) {
        _location.value = location
    }

    fun createTask() {
        viewModelScope.launch {
            // Make a new reminder
            val newReminder = Reminder(
                title = title.value.toString(),
                description = description.value.toString(),
                latitude = location.value?.latitude.toString(),
                longitude = location.value?.longitude.toString()
            )
            // Save the new reminder into DB
            locationTaskRepository.insert(reminder = newReminder)
            // Adding geofence
            _location.value?.let {
                geofenceHelper.createGeofence(location = LatLng(it.latitude, it.longitude), requestId = newReminder.id.toString())
            }
            navigationState.value = true
        }
    }


    class Factory(private val context: Context, private val locationTaskRepository: LocationTaskRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateTaskViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CreateTaskViewModel(context, locationTaskRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}

