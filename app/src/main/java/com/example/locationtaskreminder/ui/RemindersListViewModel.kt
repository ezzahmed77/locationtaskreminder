package com.example.locationtaskreminder.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.locationtaskreminder.data.model.Reminder
import com.example.locationtaskreminder.data.repository.LocationTaskRepository
import com.example.locationtaskreminder.data.repository.LocationTaskRepositoryImpl
import com.example.locationtaskreminder.helpers.randomReminders
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RemindersListViewModel(
    private val context: Context,
    private val locationTaskRepository: LocationTaskRepository
    ) : ViewModel() {

     val allReminders: StateFlow<List<Reminder>>  = locationTaskRepository.getAllReminders()
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    init {
        viewModelScope.launch {
            locationTaskRepository.deleteAllReminders()
            randomReminders.forEach{
                locationTaskRepository.insert(it)
            }
        }
    }

    // Factory
    class Factory(private val context: Context, private val locationTaskRepository: LocationTaskRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RemindersListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RemindersListViewModel(context, locationTaskRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}