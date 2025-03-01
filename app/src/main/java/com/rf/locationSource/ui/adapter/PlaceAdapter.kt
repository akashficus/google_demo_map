package com.rf.locationSource.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rf.locationSource.databinding.ItemPlaceBinding
import com.rf.locationSource.localDB.model.Place

class PlaceAdapter(
    private var placeList: MutableList<Place>,
    private val onEditClick: (Place) -> Unit,
    private val onDeleteClick: (Place) -> Unit
) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = placeList[position]
        holder.bind(place, onEditClick, onDeleteClick)
    }

    override fun getItemCount(): Int = placeList.size

    fun updateList(newList: List<Place>) {
        placeList.clear()
        placeList.addAll(newList)
        notifyDataSetChanged()
    }

    class PlaceViewHolder(private val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place, onEditClick: (Place) -> Unit, onDeleteClick: (Place) -> Unit) {
            binding.tvPlaceName.text = place.placeName
            binding.tvPlaceAddress.text = place.placeAddress

            binding.btnEdit.setOnClickListener { onEditClick(place) }
            binding.btnDelete.setOnClickListener { onDeleteClick(place) }
        }
    }
}


