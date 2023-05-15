package com.example.locationtaskreminder.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locationtaskreminder.R
import com.example.locationtaskreminder.data.locationdatabase.LocationTaskDatabase
import com.example.locationtaskreminder.data.repository.LocationTaskRepositoryImpl
import com.example.locationtaskreminder.databinding.FragmentRemindersListBinding
import com.example.locationtaskreminder.ui.adapters.ReminderAdapter
import kotlinx.coroutines.launch

class RemindersListFragment : Fragment() {

    private lateinit var binding: FragmentRemindersListBinding
    private lateinit var  remindersLisViewModel: RemindersListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReminderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_reminders_list,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get the repository
        val locationDatabase = LocationTaskDatabase.getDatabase(requireContext())
        val locationTaskDao = locationDatabase.locationTaskDao()
        val locationTaskRepository = LocationTaskRepositoryImpl(locationTaskDao = locationTaskDao)

        // Initialize ViewModel
        val factory = RemindersListViewModel.Factory(requireActivity().applicationContext, locationTaskRepository)
        remindersLisViewModel = ViewModelProvider(this, factory)[RemindersListViewModel::class.java]

        // Connect viewModel
        binding.viewModel = remindersLisViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Initialize RecyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReminderAdapter{id->
            val action = RemindersListFragmentDirections.actionRemindersListFragmentToReminderDetailsFragment(reminderId = id)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter

        // Update the UI with list of reminders
        lifecycleScope.launch {
            remindersLisViewModel.allReminders.collect{reminders->
                adapter.clearData() // Clear existing data in the adapter
                adapter.addData(reminders)
            }
        }

        // Add listener for fab button
        binding.fabAddReminder.setOnClickListener {
            findNavController().navigate(R.id.action_remindersListFragment_to_createTaskFragment)
        }

    }
}