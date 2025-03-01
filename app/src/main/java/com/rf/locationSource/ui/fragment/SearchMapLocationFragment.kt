package com.rf.locationSource.ui.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.pay2local.modem.utils.Status
import com.rf.locationSource.BuildConfig
import com.rf.locationSource.R
import com.rf.locationSource.data.repository.request.Prediction
import com.rf.locationSource.databinding.SearchMapLocationFragmentBinding
import com.rf.locationSource.localDB.model.Place
import com.rf.locationSource.ui.base.BaseFragment
import com.rf.locationSource.ui.viewmodel.GoogleMapDemoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMapLocationFragment : BaseFragment<SearchMapLocationFragmentBinding>(R.layout.search_map_location_fragment), OnMapReadyCallback {

    private val viewModel: GoogleMapDemoViewModel by viewModels()
    lateinit var placeList:List<Prediction>
    private lateinit var googleMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .build()
        )
        initializationView()
    }

    fun initializationView() {
        val mapFragment = childFragmentManager?.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mDataBinding?.editSearchPlace?.setOnItemClickListener { _, _, position, _ ->
            val selectedPlace = placeList[position]
            //viewDataBinding?.editSearchPlace?.setText(selectedPlace.description)
            fetchPlaceDetails(selectedPlace.placeId,selectedPlace.description)
        }
        mDataBinding?.editSearchPlace?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Called after the text is changed
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length>=3) {
                    setSearchMapping(s.toString())
                }
            }
        })

    }

    private fun showSaveLocationDialog(place: Place) {
        val dialog = AlertDialog.Builder(activity, R.style.TransparentDialog)
            .setView(R.layout.dialog_save_location)
            .create()

        dialog.show()

        // Set window position to top
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.TOP) // Position at the top
        }

        val saveButton = dialog.findViewById<Button>(R.id.btnSave)
        saveButton?.setOnClickListener {
            viewModel.insertPlace(place)
            dialog.dismiss()
        }
    }


    fun setSearchMapping(keyword:String) {
        viewModel.getPlaceSuggestions(keyword)
        viewModel.getPlaceSuggestionsResponseModel.observe(this){
            when(it.status){
                Status.SUCCESS->{
                    Log.d("setSearchMapping","::"+it.data)
                    placeList = it.data?.predictions ?: arrayListOf()
                    val placeDescriptions = placeList.map { prediction -> prediction.description }
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, placeDescriptions)
                    mDataBinding?.editSearchPlace?.setAdapter(adapter)
                    mDataBinding?.editSearchPlace?.showDropDown()
                }
                Status.ERROR->{}
                Status.LOADING->{
                }
            }
        }
    }

    private fun fetchPlaceDetails(placeId: String,placeName:String) {
        hideKeyBoard()

        viewModel.getPlaceDetails(placeId)
        viewModel.getPlaceDetailsResponseModel.observe(this){
            when(it.status){
                Status.SUCCESS->{
                    Log.d("fetchPlaceDetails","::"+it.data?.result?.geometry?.location?.lat)
                    it.data?.result?.geometry?.let { it1->
                        val latLng = LatLng(it1.location.lat,it1.location.lng)
                        googleMap.clear()
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(placeName)
                        )
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6f))
                        showSaveLocationDialog(Place(placeId,placeName,"",latLng.latitude,latLng.longitude))
                    }
                }
                Status.ERROR->{}
                Status.LOADING->{
                }
            }
        }
    }

    private fun hideKeyBoard() {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(mDataBinding?.editSearchPlace?.windowToken, 0)
        mDataBinding?.editSearchPlace?.setText("")
        placeList = arrayListOf()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, placeList)
        mDataBinding?.editSearchPlace?.setAdapter(adapter)
        adapter.notifyDataSetChanged()
        mDataBinding?.editSearchPlace?.dismissDropDown()
    }

    override fun onMapReady(googleMap: GoogleMap) {
      this.googleMap = googleMap
        val defaultLocation = LatLng(22.7196, 75.8577)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 6f))
    }
}