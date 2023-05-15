package com.example.locationtaskreminder.ui

import android.content.pm.PackageManager
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
import androidx.navigation.fragment.findNavController
import com.example.locationtaskreminder.R
import com.example.locationtaskreminder.databinding.FragmentSelectLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

private const val PERMISSIONS_REQUEST_LOCATION = 1

class SelectLocationFragment : Fragment() {
    // User location

    private val viewModel: SelectLocationViewModel by activityViewModels()

    private lateinit var binding: FragmentSelectLocationBinding

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */


        // Get user location
        viewModel.getUserLocation(requireContext())

        // Move the camera to location
        viewModel.location.observe(viewLifecycleOwner, Observer {
            Log.i("TAG", "From onViewCreated location is ${viewModel.location.value?.latitude}")
            // Move the camera
            val locationLatitude = viewModel.location.value?.latitude?.toDouble() ?: 0.0
            val locationLongitude = viewModel.location.value?.longitude?.toDouble() ?: 0.0
            val latLng = LatLng(locationLatitude, locationLongitude)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
            googleMap.addMarker(MarkerOptions().position(latLng).title("My Location"))
        })

        // Enable the user to add a marker to the map to select the location of the task to be reminded with it
        // Add an OnMapClickListener to the GoogleMap object
        googleMap.setOnMapClickListener { latLng ->

            // Clear any previous markers
            googleMap.clear()
            // When the map is clicked, add a marker at the clicked location
            val markerOptions = MarkerOptions().position(latLng)
            googleMap.addMarker(markerOptions)

            // Get the latitude and longitude of the clicked location
            val location = Location("")
            location.latitude = latLng.latitude
            location.longitude = latLng.longitude

            // Set the reminder location
            viewModel.setReminderLocation(location)
            Log.i("TAG", "Location is set to ${viewModel.reminderLocation.value?.latitude} && ${viewModel.reminderLocation.value?.longitude}")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_select_location,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.saveLocationButton.setOnClickListener {
            findNavController().navigate(R.id.action_selectLocationFragment_to_createTaskFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


    }


    // Call this method to get the user's location


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, get user location
                    viewModel.getUserLocation(requireContext())
                    Log.i("TAG", "From fragment the location is ${viewModel.location.value?.latitude} && ${viewModel.location.value?.longitude}")
                } else {
                    // Permission denied, handle accordingly
                    Log.d("TAG", "Location permission denied")
                }
                return
            }
        }
    }

}