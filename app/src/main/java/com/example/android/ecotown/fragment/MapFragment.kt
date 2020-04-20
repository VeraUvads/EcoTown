package com.example.android.ecotown.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.main.fragment_map.*
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
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapBinding.inflate(inflater, container, false)
        var mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?

        toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        setHasOptionsMenu(true)

        contextMap = this.context!!

        if (mapFragment == null) {
            val fm = fragmentManager
            var ft = fm?.beginTransaction()
            mapFragment = SupportMapFragment.newInstance()
            ft?.replace(R.id.map, mapFragment)?.commit()
        }

        searchView()


        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.activity!!)

        mapFragment?.getMapAsync(this)
        return binding.root
    }

    private fun searchView() {
        binding.searchView.setCursorDrawable(R.drawable.custom_cursor)

        binding.searchView.setVoiceSearch(false);
        binding.searchView.setCursorDrawable(R.drawable.custom_cursor);
        binding.searchView.setEllipsize(true);
     //   binding.searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        binding.searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location: String = binding.searchView.toString()
                var addressList = mutableListOf<Address>()
                if (location.isNotEmpty()) {
                    var geocoder   = Geocoder(contextMap)
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



        binding.searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
            }

            override fun onSearchViewShown() {
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.action_search);
        binding.searchView.setMenuItem(item);
        super.onCreateOptionsMenu(menu, inflater)
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
        val task: Task<Location> = mFusedLocationProviderClient.lastLocation
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