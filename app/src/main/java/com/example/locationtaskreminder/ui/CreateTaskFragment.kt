package com.example.locationtaskreminder.ui

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.locationtaskreminder.R
import com.example.locationtaskreminder.data.locationdatabase.LocationTaskDatabase
import com.example.locationtaskreminder.data.repository.LocationTaskRepositoryImpl
import com.example.locationtaskreminder.databinding.FragmentCreateTaskBinding
import kotlinx.coroutines.launch

class CreateTaskFragment : Fragment() {

    private lateinit var binding: FragmentCreateTaskBinding
    private lateinit var createTaskViewModel: CreateTaskViewModel
    private val selectLocationViewModel: SelectLocationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_task,
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
        val factory = CreateTaskViewModel.Factory(requireActivity().applicationContext, locationTaskRepository)
        createTaskViewModel = ViewModelProvider(this, factory)[CreateTaskViewModel::class.java]

        // Connect with binding
        binding.viewModel = createTaskViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Adding click listener for setLocation button
        binding.locationButton.setOnClickListener {
            findNavController().navigate(R.id.action_createTaskFragment_to_selectLocationFragment)
        }

        // Set location from SelectLocationFragment
        selectLocationViewModel.reminderLocation.observe(viewLifecycleOwner, Observer {
            createTaskViewModel.setLocation(it?: Location(""))
        })

        // Adding click Listener to save button to navigate to reminders list screen
        lifecycleScope.launch {
            createTaskViewModel.navigationState.collect{
                if(it){
                    findNavController().navigate(R.id.action_createTaskFragment_to_remindersListFragment)
                }
            }
        }

    }

}
