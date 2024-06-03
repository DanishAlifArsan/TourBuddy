package com.tourbuddy.ui.Main

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tourbuddy.ListDestinationAdapter
import com.tourbuddy.OnboardingActivity
import com.tourbuddy.R
import com.tourbuddy.ui.Main.MainViewModel
import com.tourbuddy.api.DestinationResponseItem
import com.tourbuddy.data.Destination
import com.tourbuddy.databinding.ActivityMainBinding
import com.tourbuddy.pref.UserPreference
import com.tourbuddy.pref.dataStore
import com.tourbuddy.viewModel.DestinationViewModel
import com.tourbuddy.viewModel.DestinationViewModelFactory
import com.tourbuddy.viewModel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale


class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    private lateinit var binding : ActivityMainBinding
    private lateinit var rvDestination: RecyclerView
    private lateinit var listDestinationAdapter : ListDestinationAdapter
    private lateinit var auth: FirebaseAuth
    private val list = ArrayList<DestinationResponseItem>()
    private lateinit var destinationViewModel : DestinationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        destinationViewModel = obtainViewModel(this@MainActivity)

        rvDestination = binding.rvDestination
        rvDestination.setHasFixedSize(true)

        destinationViewModel.getAllDestination("Indonesia").observe(this) {
            list.addAll(it.destinationResponse)
            showRecyclerlist()
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
                    filterList(newText.toString())
                    return true
                }
            })
        }

        binding.ivProfile.setOnClickListener {
            showMenu(it)
        }

        binding.btnLocation.setOnClickListener{
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            getMyLastLocation()
        }
    }

    private fun getDestinationList(): ArrayList<Destination>{
        val dataName = resources.getStringArray(R.array.data_name)
        val dataLocation = resources.getStringArray(R.array.data_location)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listDestination = arrayListOf<Destination>()
        for (i in dataName.indices) {
            val destination = Destination(dataName[i], dataLocation[i], dataPhoto.getResourceId(i, -1))
            listDestination.add(destination)
        }
        return listDestination
    }

    private fun showRecyclerlist() {
        rvDestination.layoutManager = LinearLayoutManager(this)
        listDestinationAdapter = ListDestinationAdapter(list)
        rvDestination.adapter = listDestinationAdapter
    }

    private fun filterList(filter : String) {
        val filteredList = ArrayList<DestinationResponseItem>()
        for (item in list) {
            if (item.name.toString().lowercase().contains(filter.lowercase()) ||
                item.city.toString().lowercase().contains(filter.lowercase())) {
                filteredList.add(item)
            }
        }
        if (filteredList.isNotEmpty()) {
            listDestinationAdapter.setFilteredList(filteredList)
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
            R.id.action_profile -> { // todo profile
                true
            }
            R.id.action_bookmark -> { // to do bookmark
                true
            }
            R.id.action_signout -> {
                viewModel.logout()
                startActivity(Intent(this@MainActivity, OnboardingActivity::class.java))
                finish()
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
                }
            }
        }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                var currentLocation = "Indonesia"
                Log.d("TAG", location?.latitude.toString())
                if (location != null) {
                    val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                    Log.d("TAG", "getMyLastLocation: ")
                    geocoder.getAddress(location.latitude, location.longitude) {
                        if(it != null) {
                            Log.d("TAG", "getAddress: ")
                            currentLocation = it.subAdminArea
                        }
                    }
                }
                binding.btnLocation.text = currentLocation
                destinationViewModel.getAllDestination(currentLocation).observe(this) {
                    list.addAll(it.destinationResponse)
                    showRecyclerlist()
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

    @Suppress("DEPRECATION")
    fun Geocoder.getAddress(
        latitude: Double,
        longitude: Double,
        address: (Address?) -> Unit
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
}