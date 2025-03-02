package com.rf.locationSource.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.rf.locationSource.R
import com.rf.locationSource.databinding.GoogleMapLocationFragmentBinding
import com.rf.locationSource.databinding.GoogleMapPathFragmentBinding
import com.rf.locationSource.ui.base.BaseFragment
import com.rf.locationSource.ui.viewmodel.GoogleMapDemoViewModel
import org.json.JSONObject

class GoogleMapPathFragment : BaseFragment<GoogleMapPathFragmentBinding>(R.layout.google_map_path_fragment),
    OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap

    private val viewModel: GoogleMapDemoViewModel by viewModels()

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
        viewModel.placeList.observe(viewLifecycleOwner) { placeList ->
            if (placeList != null && placeList.isNotEmpty()) {
                val polylineOptions = PolylineOptions()
                polylineOptions.add(LatLng(placeList[0].placeLatitude!!, placeList[0].placeLongitude!!))
                if(placeList.size>2){
                    val wayList: List<LatLng> = placeList.subList(1, placeList.size - 1)
                        .map { LatLng(it.placeLatitude!!, it.placeLongitude!!) }
                    wayList.forEach {
                        polylineOptions.add(it)
                    }
                }
                polylineOptions.add(LatLng(placeList.last().placeLatitude!!, placeList.last().placeLongitude!!))
                polylineOptions .color(Color.BLUE)
                        .width(8f)
                googleMap.addPolyline(polylineOptions)

            } else {
                Log.d("SearchMapLocationActivity", "No places found in database.")
            }
        }
    }


}