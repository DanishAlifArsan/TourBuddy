package com.tourbuddy.ui.Main

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.tourbuddy.ui.OnboardingActivity
import com.tourbuddy.R
import com.tourbuddy.api.response.ListDestinationsItem
import com.tourbuddy.databinding.ActivityMainBinding
import com.tourbuddy.viewModelFactory.ViewModelFactory
import java.util.Locale


class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    private lateinit var binding : ActivityMainBinding
    private lateinit var rvDestination: RecyclerView
    private lateinit var listDestinationAdapter : ListDestinationAdapter
    private val list = ArrayList<ListDestinationsItem>()
    private lateinit var destinationViewModel : DestinationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest : LocationRequest
    private lateinit var locationCallback : LocationCallback
    private var currentLocation : Location? = null
    private var requestingLocationUpdates = false
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateValuesFromBundle(savedInstanceState)

        destinationViewModel = obtainViewModel(this@MainActivity)
        listDestinationAdapter = ListDestinationAdapter()

        rvDestination = binding.rvDestination
        rvDestination.setHasFixedSize(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        createLocationRequest()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                setupLocation(locationResult.locations[0])
            }
        }

        destinationViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(binding){
            searchBar.clearFocus()
            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    listDestinationAdapter.filter.filter(newText)
                    return true
                }
            })
        }

        binding.ivProfile.setOnClickListener {
            showMenu(it)
        }
        binding.layoutLocation.setOnClickListener{
            createLocationRequest()
        }
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdate()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates)
        super.onSaveInstanceState(outState)
    }

    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        savedInstanceState ?: return

        if(savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
            requestingLocationUpdates = savedInstanceState.getBoolean(
                REQUESTING_LOCATION_UPDATES_KEY)
        }
    }

    fun showMenu(v: View) {
        PopupMenu(this, v).apply {
            setOnMenuItemClickListener(this@MainActivity)
            inflate(R.menu.item_menu)
            show()
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
//            R.id.action_profile -> { // todo profile
//                true
//            }
//            R.id.action_bookmark -> { // todo bookmark
//                true
//            }
            R.id.action_signout -> {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.signout))
                    setMessage(getString(R.string.hint_signout))
                    setPositiveButton(getString(R.string.signout)){ _, _ ->
                        viewModel.logout()
                        val intent = Intent(this@MainActivity, OnboardingActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    setNegativeButton(getString(R.string.cancel)) { _,_ ->
                        setCancelable(true)
                    }
                    create()
                    show()
                }
                true
            }
            else -> false
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity) : DestinationViewModel {
        val factory = DestinationViewModelFactory.getInstance(activity)
        return ViewModelProvider(activity, factory).get(DestinationViewModel::class.java)
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                    Toast.makeText(this, getString(R.string.error_permission), Toast.LENGTH_SHORT).show()
                    requestingLocationUpdates = false
                }
            }
        }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(UPDATE_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client : SettingsClient = LocationServices.getSettingsClient(this)
        val task : Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            getMyLastLocation()
        }
        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    it.startResolutionForResult(this@MainActivity, REQUEST_CHECK_SETTINGS)
                } catch (e : IntentSender.SendIntentException) {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getMyLastLocation() {
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            requestingLocationUpdates = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                Log.d("TAG", "${location?.latitude},${location?.longitude}")
                if (location != null) {
                    setupLocation(location)
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun setupLocation(location : Location) {
        stopLocationUpdates()
        currentLocation = location
        val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
        Log.d("TAG", "getMyLastLocation: ")
        geocoder.getAddress(location.latitude, location.longitude) {
            if(it != null) {
                Log.d("TAG", "getAddress: ")
                val city = it.subAdminArea
                val lat = it.latitude.toFloat()
                val lon = it.longitude.toFloat()
//                val lat =  -8.102616f
//                val lon = 110.456204f
                binding.tvLocation.text = city
                binding.rvDestination.layoutManager = LinearLayoutManager(this)
                destinationViewModel.getAllDestination(lat, lon).observe(this) {response ->
                    listDestinationAdapter.addToList(response.listDestinations)
                    rvDestination.adapter = listDestinationAdapter
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    fun Geocoder.getAddress(
        latitude: Double,
        longitude: Double,
        address: (Address?) -> Unit
    ) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            getFromLocation(latitude, longitude, 1) {
                address(it.firstOrNull())
            }
            return
        }

        try {
            address(getFromLocation(latitude, longitude, 1)?.firstOrNull())
        } catch(e: Exception) {
            address(null)
        }
    }

    private fun startLocationUpdate() {
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    //setting lokasi aktif
                    getMyLastLocation()
                }
                Activity.RESULT_CANCELED -> {
                    //setting lokasi mati
                    Toast.makeText(this, getString(R.string.error_location), Toast.LENGTH_SHORT).show()
                    requestingLocationUpdates = false
                }
            }
        }
    }

    companion object {
        const val REQUEST_CHECK_SETTINGS = 0x1
        const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
        const val REQUESTING_LOCATION_UPDATES_KEY = "Request_Location_Update"
    }
}