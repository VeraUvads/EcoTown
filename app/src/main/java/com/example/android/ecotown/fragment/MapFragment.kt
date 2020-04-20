package com.example.android.ecotown.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import br.com.mauker.materialsearchview.MaterialSearchView
import com.example.android.ecotown.Models.Store

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.IOException


class MapFragment : Fragment(), OnMapReadyCallback {

    lateinit var storeList: MutableList<Store>
    lateinit var suggestionsList: MutableList<String>
    lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap

    private lateinit var contextMap: Context

    lateinit var currentLocation: Location
    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private val pReqCode = 101
    private lateinit var toolbar: Toolbar

    private val firstKey: String = "16EARRAwrnOfWNhx_Z2snbLxyWX6QO7SUnF-BZzJv4KM"
    private val secondKey: String = "Лист1"

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
        getStoresList()

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.activity!!)

        mapFragment?.getMapAsync(this)
        return binding.root
    }

    private fun getStoresList() {
        val database = FirebaseDatabase.getInstance()

        val databaseReference = database.getReference(firstKey).child(secondKey)

        val checkListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.w("Vera", "loadPost:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                storeList = mutableListOf()
                suggestionsList = mutableListOf()
                for (postSnap: DataSnapshot in p0.children) {
                    val id = postSnap.child("id").value.toString()
                    val building = postSnap.child("building").value.toString()
                    val description = postSnap.child("description").value.toString()
                    val latitude = postSnap.child("latitude").value.toString()
                    val longtitude = postSnap.child("longtitude").value.toString()
                    val name = postSnap.child("name").value.toString()
                    val theme = postSnap.child("theme").value.toString()

                    val store = Store(id, building, description, latitude, longtitude, name, theme)
                    storeList.add(store)
                    suggestionsList.add(building)
                    suggestionsList.add(theme)
                    suggestionsList.add(name)
                    binding.searchView.clearSuggestions()
                    binding.searchView.addSuggestions(suggestionsList)
                }
            }
        }
        databaseReference.addValueEventListener(checkListener)

    }

    private fun searchView() {

        binding.searchView.setBackgroundColor(resources.getColor(R.color.bg_searchView))
        binding.searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                val location: String = binding.searchView.toString()
//                var addressList = mutableListOf<Address>()
//                if (location.isNotEmpty()) {
//                    var geocoder = Geocoder(contextMap)
//                    try {
//                        addressList = geocoder.getFromLocationName(location, 1)
//
//                    } catch (e: IOException) {
//                    }
//                    var address: Address = addressList[0]
//                    val newLocation = LatLng(address.latitude, address.longitude)
//                    mMap.addMarker(MarkerOptions().position(newLocation).title("New Location"))
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 10f))
//                    Log.i("Vera", "${newLocation.latitude} ${newLocation.longitude}")
//                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        binding.searchView.setOnItemClickListener { parent, view, position, id ->
            mMap.clear()
            var suggestion = binding.searchView.getSuggestionAtPosition(position);

            binding.searchView.setQuery(suggestion, false)

            binding.searchView.closeSearch()

            val nameFilter = storeList.filter { it.name.contains(suggestion) }

            if (nameFilter.isNotEmpty()) {
                val newLocation =
                    LatLng(nameFilter[0].latitude.toDouble(), nameFilter[0].longtitude.toDouble())
                mMap.addMarker(MarkerOptions().position(newLocation).title("New Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15f))
                Log.i("Vera", "${newLocation.latitude} ${newLocation.longitude}")
            }

            val otherFilter =
                storeList.filter { it.building.contains(suggestion) || it.theme.contains(suggestion) }

            if (otherFilter.isNotEmpty()) {
                for (i in otherFilter) {
                    val newLocation =
                        LatLng(i.latitude.toDouble(), i.longtitude.toDouble())
                    mMap.addMarker(MarkerOptions().position(newLocation).title(i.name))
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude, currentLocation.longitude), 14f))
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_search) {
            binding.searchView.openSearch()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == Activity.RESULT_OK) {
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (matches != null && matches.size > 0) {
                val searchWrd = matches[0]
                if (!TextUtils.isEmpty(searchWrd)) {
                    binding.searchView.setQuery(searchWrd, false)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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