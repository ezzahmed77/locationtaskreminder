package com.example.locationtaskreminder.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.locationtaskreminder.data.model.Reminder
import com.example.locationtaskreminder.data.repository.LocationTaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ReminderDetailsViewModel(
    private val context: Context,
    private val locationTaskRepository: LocationTaskRepository,
    private val reminderId: Int,
    ): ViewModel(){

    val detailsReminder: StateFlow<Reminder> = locationTaskRepository.getReminder(id = reminderId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Reminder(title = "", description = "", latitude = "", longitude = "")
        )

    class Factory(private val context: Context, private val locationTaskRepository: LocationTaskRepository, private val reminderId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReminderDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReminderDetailsViewModel(context, locationTaskRepository, reminderId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}