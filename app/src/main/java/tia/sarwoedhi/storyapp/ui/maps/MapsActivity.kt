package tia.sarwoedhi.storyapp.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import tia.sarwoedhi.storyapp.R
import tia.sarwoedhi.storyapp.databinding.ActivityMapsBinding
import tia.sarwoedhi.storyapp.utils.UiState

@AndroidEntryPoint
class MapsActivity : AppCompatActivity() {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val viewModel: MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.list_story_by_location)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(mapCallback)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                getDeviceLocation()
            }
        }

    private val mapCallback = OnMapReadyCallback { googleMap ->
        mGoogleMap = googleMap

        googleMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        getDeviceLocation()
        setMapStyle()
        storyMarkersLocation()

    }

    private fun setMapStyle() {

        try {
            mGoogleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.map_style
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mGoogleMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.activate_location_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun storyMarkersLocation() {
        viewModel.getAllStoriesWithLocation()
        viewModel.mapStories.observe(this) { resource ->
            when (resource) {
                is UiState.Success -> {
                    resource.data?.forEach { story ->
                        if (story.lat != null && story.lon != null) {
                            val latLng = LatLng(story.lat, story.lon)
                            mGoogleMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(story.name)
                                    .snippet("Lat: ${story.lat}, Lng: ${story.lon}")
                            )
                        }
                    }
                }

                is UiState.Loading -> {

                }

                is UiState.Error -> {
                    Toast.makeText(this, resource.error, Toast.LENGTH_LONG).show()
                }
            }

        }
    }


}