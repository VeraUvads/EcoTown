package com.example.android.ecotown.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.example.android.ecotown.R
import com.example.android.ecotown.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task


class MapFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap

    private lateinit var contextMap: Context

    lateinit var currentLocation: Location
    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private val pReqCode = 101


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapBinding.inflate(inflater, container, false)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        var mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?

        contextMap = this.context!!

        if (mapFragment == null) {
            val fm = fragmentManager
            var ft = fm?.beginTransaction()
            mapFragment = SupportMapFragment.newInstance()
            ft?.replace(R.id.map, mapFragment)?.commit()
        }

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.activity!!)



        mapFragment?.getMapAsync(this)
        return binding.root

    }

    private fun fetchLastLocation() {
        if (ContextCompat.checkSelfPermission(
                contextMap,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                pReqCode
            )
        }
        var task: Task<Location> = mFusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                currentLocation = it
                val current = LatLng(currentLocation.latitude, currentLocation.longitude)
                val marker: MarkerOptions = MarkerOptions().position(current).title("I am Here")
                mMap.addMarker(marker)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15f))
                Toast.makeText(contextMap, "${currentLocation.latitude}  ${currentLocation.longitude}", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {

        mMap = p0
        fetchLastLocation()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == pReqCode) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                fetchLastLocation()
            }
        }
    }
}