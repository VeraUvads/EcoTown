package com.example.android.ecotown.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.MatrixCursor
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.CursorAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.android.ecotown.Adapters.SearchAdapter

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
import java.io.IOException
import java.lang.Exception


class MapFragment : Fragment(), OnMapReadyCallback {

    //    lateinit var adressList: MutableList<Address>
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

        binding.svLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location: String = binding.svLocation.query.toString()
                var addressList = mutableListOf<Address>()
                if (location.isNotEmpty()) {
                    var geocoder = Geocoder(contextMap)
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)

                    } catch (e: IOException) {
                    }
                    var address: Address = addressList[0]
                    val newLocation = LatLng(address.latitude, address.longitude)
                    mMap.addMarker(MarkerOptions().position(newLocation).title("New Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 10f))
                    Log.i("Vera", "${newLocation.latitude} ${newLocation.longitude}")
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        mapFragment?.getMapAsync(this)
        return binding.root
    }

    private fun loadHistory(query: String) {
        var items = mutableListOf<String>()
        items.add("test 1")
        items.add("test 2")

        var columns = mutableListOf<String>()
        columns.add("id")
        columns.add("text")
        var temp = mutableListOf<Any>()
        temp.add(0)
        temp.add("default")

        var cursor = MatrixCursor(columns.toTypedArray())
        for(i in items) {

            temp[0] = i
            temp[1] = i

            cursor.addRow(temp);
        }
        binding.svLocation.suggestionsAdapter = SearchAdapter(contextMap, cursor, items)


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
                Toast.makeText(
                    contextMap,
                    "${currentLocation.latitude}  ${currentLocation.longitude}",
                    Toast.LENGTH_SHORT
                ).show()

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