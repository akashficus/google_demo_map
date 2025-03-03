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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import com.rf.locationSource.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
/// This fragment is responsible for displaying a Google Map and handling location search functionality.
@AndroidEntryPoint
class SearchMapLocationFragment : BaseFragment<SearchMapLocationFragmentBinding>(R.layout.search_map_location_fragment), OnMapReadyCallback {

    private val viewModel: GoogleMapDemoViewModel by viewModels()
    lateinit var placeList: List<Prediction>
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

    /// Initializes the view by setting up the map and search field interactions.
    fun initializationView() {
        val mapFragment = childFragmentManager?.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mDataBinding?.editSearchPlace?.setOnItemClickListener { _, _, position, _ ->
            val selectedPlace = placeList[position]
            fetchPlaceDetails(selectedPlace.placeId, selectedPlace.description)
        }

        // Adds a text change listener to trigger location search when typing.
        mDataBinding?.editSearchPlace?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length >= 3) {
                    setSearchMapping(s.toString())
                }
            }
        })
    }

    /// Displays a dialog for saving the selected location.
    private fun showSaveLocationDialog(place: Place) {
        val dialog = AlertDialog.Builder(activity, R.style.TransparentDialog)
            .setView(R.layout.dialog_save_location)
            .create()

        dialog.show()

        // Positions the dialog at the top of the screen.
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.TOP)
        }

        val saveButton = dialog.findViewById<Button>(R.id.btnSave)
        saveButton?.setOnClickListener {
            lifecycleScope.launch {
                val firstPlace = viewModel.getFirstPlaceOrder()
                firstPlace?.let {
                    place.placeDistance = Constant.calculateDistance(firstPlace, place)
                }
                viewModel.insertPlace(place)
                findNavController().popBackStack()
                findNavController().navigate(R.id.googleMapLocationFragment)
            }
            dialog.dismiss()
        }
    }

    /// Fetches and updates the list of places matching the given search keyword.
    fun setSearchMapping(keyword: String) {
        viewModel.getPlaceSuggestions(keyword)
        viewModel.getPlaceSuggestionsResponseModel.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    placeList = it.data?.predictions ?: arrayListOf()
                    val placeDescriptions = placeList.map { prediction -> prediction.description }
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, placeDescriptions)
                    mDataBinding?.editSearchPlace?.setAdapter(adapter)
                    mDataBinding?.editSearchPlace?.showDropDown()
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }

    /// Fetches detailed information about the selected place.
    private fun fetchPlaceDetails(placeId: String, placeName: String) {
        hideKeyBoard()

        viewModel.getPlaceDetails(placeId)
        viewModel.getPlaceDetailsResponseModel.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.result?.geometry?.let { it1 ->
                        val latLng = LatLng(it1.location.lat, it1.location.lng)
                        googleMap.clear()
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(placeName)
                        )
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
                        showSaveLocationDialog(Place(placeId, placeName, "", latLng.latitude, latLng.longitude))
                    }
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }

    /// Hides the keyboard and resets the search input field.
    private fun hideKeyBoard() {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(mDataBinding.editSearchPlace.windowToken, 0)
        mDataBinding.editSearchPlace.setText("")
        placeList = arrayListOf()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, placeList)
        mDataBinding.editSearchPlace.setAdapter(adapter)
        adapter.notifyDataSetChanged()
        mDataBinding.editSearchPlace.dismissDropDown()
    }

    /// Called when the Google Map is ready, setting the default center location.
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val defaultLocation = LatLng(21.1458, 79.0882) // Nagpur center
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 5f))
    }
}
