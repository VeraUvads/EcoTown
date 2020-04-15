package com.example.android.ecotown.fragment

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.ActionBar
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.*
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat

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
import kotlinx.android.synthetic.main.fragment_map.view.*
import kotlinx.android.synthetic.main.toolbar_search_view.view.*
import java.io.IOException
import java.lang.Exception
import kotlin.time.nanoseconds


class MapFragment : Fragment(), OnMapReadyCallback {

    //    lateinit var adressList: MutableList<Address>
    lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap

    private lateinit var contextMap: Context

    lateinit var currentLocation: Location
    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private val pReqCode = 101

    lateinit var mToolbar: Toolbar

    lateinit var mSearchItem: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapBinding.inflate(inflater, container, false)


        var mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?


        mToolbar = binding.root.findViewById(R.id.toolbar2)
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.inflateMenu(R.menu.material_search_view)
            var menu: Menu = mToolbar.menu
        }

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.material_search_view, menu)
        mSearchItem = menu.findItem(R.id.m_search)
        mSearchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                animateSearchToolbar(1, containsOverflow = true, show = true)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                if (mSearchItem.isActionViewExpanded) {
                    animateSearchToolbar(1, containsOverflow = false, show = false)
                }
                return true
            }
        })
    }

    private fun animateSearchToolbar(
        numberOfMenuIcon: Int,
        containsOverflow: Boolean,
        show: Boolean
    ) {

        mToolbar.setBackgroundColor(ContextCompat.getColor(contextMap, android.R.color.white))
        if (show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                var first =
                    if (containsOverflow) resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) else 0
                var second =
                    resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) * numberOfMenuIcon / 2
                var width: Int = mToolbar.width.minus(first.minus(second))

                var rtl = if (isRtl(resources)) mToolbar.width - width else width

                var createCircularReveal: Animator =
                    ViewAnimationUtils.createCircularReveal(
                        mToolbar, rtl, mToolbar.height / 2, 0.0f,
                        width.toFloat()
                    )
                createCircularReveal.duration = 220
                createCircularReveal.start()
            } else {
                var translateAnimation =
                    TranslateAnimation(0.0f, 0.0f, -mToolbar.height.toFloat(), 0.0f)
                translateAnimation.duration = 220
                mToolbar.clearAnimation()
                mToolbar.startAnimation(translateAnimation)

            }
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                var first =
                    if (containsOverflow) resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) else 0
                var second =
                    resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) * numberOfMenuIcon / 2
                var width: Int = mToolbar.width.minus(first.minus(second))
                var rtl = if (isRtl(resources)) mToolbar.width - width else width

                var createCircularReveal: Animator =
                    ViewAnimationUtils.createCircularReveal(
                        mToolbar, rtl, mToolbar.height / 2, 0.0f,
                        width.toFloat()
                    )
                createCircularReveal.duration = 220
             //   createCircularReveal.addListener
                }
            }

    }

    private fun isRtl(resources: Resources): Boolean {
        return resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
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