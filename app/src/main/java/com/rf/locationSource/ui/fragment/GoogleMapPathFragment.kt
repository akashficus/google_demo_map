package com.rf.locationSource.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.pay2local.modem.utils.Status
import com.rf.locationSource.R
import com.rf.locationSource.databinding.GoogleMapLocationFragmentBinding
import com.rf.locationSource.databinding.GoogleMapPathFragmentBinding
import com.rf.locationSource.localDB.model.Place
import com.rf.locationSource.ui.base.BaseFragment
import com.rf.locationSource.ui.viewmodel.GoogleMapDemoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
/// GoogleMapPathFragment is responsible for displaying the map and drawing the route based on selected places.
/// It utilizes Google Maps API to fetch directions and display them on the map.
@AndroidEntryPoint
class GoogleMapPathFragment :
    BaseFragment<GoogleMapPathFragmentBinding>(R.layout.google_map_path_fragment),
    OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap

    private val viewModel: GoogleMapDemoViewModel by activityViewModels()
    var placeList: List<Place> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .build()
        )
        initializationView()
        // observePlaceList()
    }

    fun initializationView() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap;
        observePlaceList()

    }

    private fun observePlaceList() {
        lifecycleScope.launch {
            placeList = viewModel.getPlaceListMapping()
            if (placeList.size > 2) {
                 val origin =
                    "${placeList.first().placeLatitude},${placeList.first().placeLongitude}"
                val destination =
                    "${placeList.last().placeLatitude},${placeList.last().placeLongitude}"
                val waypoints = placeList.subList(1, placeList.size - 1)
                    .map { "${it.placeLatitude},${it.placeLongitude}" }
                val waypointsParam = waypoints?.joinToString("|") ?: ""
                fetchGoogleDirections(origin, destination, waypointsParam)
            }
        }

    }

    private fun fetchGoogleDirections(origin: String, destination: String, waypoints: String) {
        Log.d("GoogleAPI", "Origin: $origin, Destination: $destination, Waypoints: $waypoints")

        viewModel.getDirections(origin, destination, waypoints)
        viewModel.getDirectionsResponseModel.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d("fetchPlaceDetails", "::" + it.data?.routes)
                    it.data?.routes?.firstOrNull()?.overview_polyline?.let { it1 ->
                        drawPolyline(it1.points)
                    }
                }

                Status.ERROR -> {}
                Status.LOADING -> {
                }
            }
        }
    }

    private fun drawPolyline(encodedPolyline: String) {
        val polylineOptions = PolylineOptions()
            .addAll(PolyUtil.decode(encodedPolyline))
            .width(10f)
            .color(Color.BLUE)
            .geodesic(true)

        googleMap.addPolyline(polylineOptions)

        val boundsBuilder = LatLngBounds.Builder()

        val origin = LatLng(placeList.first().placeLatitude!!,placeList.first().placeLongitude!!)
        googleMap.addMarker(
            MarkerOptions().position(origin).title(placeList.first().placeName).icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            )
        )
        boundsBuilder.include(origin)
        val destination = LatLng(placeList.last().placeLatitude!!,placeList.last().placeLongitude!!)
        googleMap.addMarker(
            MarkerOptions().position(destination).title(placeList.last().placeName).icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            )
        )
        boundsBuilder.include(destination)
        val bounds = boundsBuilder.build()
        val padding = 100 // Padding in pixels to avoid edges cut-off
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
    }


}