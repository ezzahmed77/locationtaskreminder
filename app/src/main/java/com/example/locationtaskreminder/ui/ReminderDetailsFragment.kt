package com.example.locationtaskreminder.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.locationtaskreminder.R
import com.example.locationtaskreminder.data.locationdatabase.LocationTaskDatabase
import com.example.locationtaskreminder.data.repository.LocationTaskRepositoryImpl
import com.example.locationtaskreminder.databinding.FragmentCreateTaskBinding
import com.example.locationtaskreminder.databinding.FragmentReminderDetailsBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ReminderDetailsFragment : Fragment() {
    private lateinit var binding: FragmentReminderDetailsBinding
    private lateinit var reminderDetailsViewModel: ReminderDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_reminder_details,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get reminder Id to show details
        val reminderId = ReminderDetailsFragmentArgs.fromBundle(requireArguments()).reminderId
        // Get the repository
        val locationDatabase = LocationTaskDatabase.getDatabase(requireContext())
        val locationTaskDao = locationDatabase.locationTaskDao()
        val locationTaskRepository = LocationTaskRepositoryImpl(locationTaskDao = locationTaskDao)

        // Initialize ViewModel
        val factory = ReminderDetailsViewModel.Factory(requireActivity().applicationContext, locationTaskRepository, reminderId)
        reminderDetailsViewModel = ViewModelProvider(this, factory)[ReminderDetailsViewModel::class.java]

        // Connect with binding
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycleScope.launch {
            reminderDetailsViewModel.detailsReminder.collect{
                binding.reminder = it
                val latitudeDouble = it.latitude.toDoubleOrNull()
                val longitudeDouble = it.longitude.toDoubleOrNull()
                binding.latitude = String.format("%.2f", latitudeDouble)
                binding.longitude = String.format("%.2f", longitudeDouble)
            }
        }

    }

}