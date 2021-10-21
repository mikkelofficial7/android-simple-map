package com.example.myapplication

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.myapplication.Model.PlaceResponse
import com.example.myapplication.Model.UserLocation
import com.example.myapplication.Utility.GpsUtil
import com.example.myapplication.Utility.PermissionUtil
import com.example.myapplication.service.NetworkCall
import com.example.myapplication.service.NetworkInterface
import com.example.myapplication.service.ResponseCallback
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


open class MainActivity : FragmentActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private lateinit var geocoder: Geocoder

    private var zooming = 14f
    private var tilting = 90f
    private var circleRadius = 200.0

    private var userLocation = UserLocation()
    private var listLocation = ArrayList<UserLocation>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        geocoder = Geocoder(this, Locale.getDefault())

        PermissionUtil.checkLocationPermission(this, onPermissionGranted = {
            getUserLocation()
        })

        setupClick()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            PermissionUtil.LOCATION_REQ_CODE -> {
                val isGrant = PermissionUtil.isAllPermissionGranted(grantResults)

                if (isGrant) {
                    if (GpsUtil.isGpsActive(this)) {
                        getUserLocation()
                    } else {
                        GpsUtil.showDialogGps(this)
                    }
                } else {
                    PermissionUtil.showDialogPermissionDenied(this)
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setupClick() {
        iv_zoom_in.setOnClickListener {
            zooming += 1f

            moveCameraAndSetMarker()
        }

        iv_zoom_out.setOnClickListener {
            if(zooming < 1f) 0f else zooming -= 1f

            moveCameraAndSetMarker()
        }
    }

    private fun getUserLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        PermissionUtil.checkLocationPermission(this, onPermissionGranted = {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    setUserLocation(location)
                    startMap()
                }
                .addOnFailureListener {
                    startMap()
                }
        })
    }

    private fun startMap() {
        val mapFragment: SupportMapFragment? = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap == null) {
            Toast.makeText(this, getString(R.string.unable_create_map), Toast.LENGTH_SHORT).show()
            return
        }

        mMap = googleMap

        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap?.uiSettings?.isZoomGesturesEnabled = true
        mMap?.uiSettings?.isCompassEnabled = true

        PermissionUtil.checkLocationPermission(this, onPermissionGranted = {
            mMap?.isMyLocationEnabled = true
        })

        moveCameraAndSetMarker()
    }

    private fun moveCameraAndSetMarker() {
        populateSomeLocation()

        listLocation.forEach {
            val latLng = LatLng(it.latitude, it.longitude)

            val cameraPosition = CameraPosition.Builder()
                .target(latLng) // Sets the center of the map to location user
                .zoom(zooming) // Sets the zoom
                .bearing(0f) // Sets the orientation of the camera to east
                .tilt(tilting) // Sets the tilt of the camera to 30 degrees
                .build() // Creates a CameraPosition from the builder

            mMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            mMap?.addMarker(MarkerOptions().position(latLng).title(it.title))
        }
    }

    private fun getLocationByLatitudeLongitude(latitude: Double, longitude: Double): MutableList<Address> {
        return geocoder.getFromLocation(latitude, longitude, 1)
    }

    private fun setUserLocation(location: Location) {
        val userAddress = getLocationByLatitudeLongitude(location.latitude, location.longitude)

        userLocation.title = getString(R.string.its_me)
        userLocation.description = getString(R.string.its_me)
        userLocation.fullAddress = userAddress[0].getAddressLine(0) ?: userAddress[0].adminArea
        userLocation.latitude = location.latitude
        userLocation.longitude = location.longitude
    }

    private fun populateSomeLocation() {
        listLocation.add(userLocation)
    }

    private fun loadOtherLocation() {
        val request = NetworkCall.provideRequest(NetworkInterface::class.java).getLocation(keyword = "rawa", apikey = getString(R.string.google_maps_key))
        NetworkCall.process(request, object: ResponseCallback<PlaceResponse> {
            override fun onSuccess(responseBody: PlaceResponse) {

            }

        })
    }

    private fun addCircle(latLng: LatLng) {
        mMap?.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(circleRadius)
                .strokeColor(resources.getColor(R.color.teal_200))
                .fillColor(resources.getColor(R.color.teal_200))
        )
    }
}