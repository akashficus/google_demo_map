package com.rf.locationSource.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.rf.locationSource.R
import com.rf.locationSource.ui.base.BaseActivity
import com.rf.locationSource.ui.viewmodel.GoogleMapDemoViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rf.locationSource.databinding.GoogleMapLocationFragmentBinding
import com.rf.locationSource.localDB.model.Place
import com.rf.locationSource.ui.adapter.PlaceAdapter
import com.rf.locationSource.ui.base.BaseFragment
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GoogleMapLocationFragment : BaseFragment<GoogleMapLocationFragmentBinding>(R.layout.google_map_location_fragment) {

    private val viewModel: GoogleMapDemoViewModel by viewModels()
    private lateinit var placeAdapter: PlaceAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializationView()
    }

    private fun initializationView() {
        setupRecyclerView()
        observePlaceList()
        mDataBinding.btnAddLocation.setOnClickListener {
            findNavController().navigate(R.id.action_googleMapLocationFragment_to_searchMapLocationFragment)
        }
        mDataBinding.btnAddPOI.setOnClickListener {
            findNavController().navigate(R.id.action_googleMapLocationFragment_to_searchMapLocationFragment)
        }

        mDataBinding.fabShowPath.setOnClickListener{
            findNavController().navigate(R.id.action_googleMapLocationFragment_to_googleMapPathFragment)
        }
    }

    private fun setupRecyclerView() {
        placeAdapter = PlaceAdapter(mutableListOf(),
            onEditClick = { place -> editPlace(place) },
            onDeleteClick = { place -> deletePlace(place) }
        )
        mDataBinding.rvPlaces.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = placeAdapter
        }

    }

    private fun observePlaceList() {
        viewModel.placeList.observe(viewLifecycleOwner) { placeList ->
            if (placeList != null && placeList.isNotEmpty()) {
                if (placeList.size>2){
                    mDataBinding.fabShowPath.show()
                }
                mDataBinding.rvPlaces.visibility = View.VISIBLE
                mDataBinding.tvEmptyMessage.visibility = View.GONE
                mDataBinding.btnAddPOI.visibility =View.GONE
                mDataBinding.btnAddLocation.visibility = View.VISIBLE
                placeAdapter.updateList(placeList)
            } else {
                mDataBinding.btnAddPOI.visibility =View.VISIBLE
                mDataBinding.rvPlaces.visibility = View.GONE
                mDataBinding.btnAddLocation.visibility = View.INVISIBLE
                mDataBinding.tvEmptyMessage.visibility = View.VISIBLE
                Log.d("SearchMapLocationActivity", "No places found in database.")
            }
        }
    }

    private fun editPlace(place: Place) {
        // Handle edit action (Show a dialog or navigate to another activity)
        Toast.makeText(activity, "Edit: ${place.placeName}", Toast.LENGTH_SHORT).show()
    }

    private fun deletePlace(place: Place) {
       AlertDialog.Builder(activity)
            .setTitle("Delete Place")
            .setMessage("Are you sure you want to delete ${place.placeName}?")
            .setPositiveButton("Yes") { _, _ ->
                lifecycleScope.launch {
                    viewModel.deletePlace(place)
                    findNavController().navigate(R.id.googleMapLocationFragment)
                }

            }
            .setNegativeButton("No", null)
            .show()
    }
}