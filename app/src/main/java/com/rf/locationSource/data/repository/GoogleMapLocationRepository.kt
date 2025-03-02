package com.rf.locationSource.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.google.gson.JsonObject
import com.rf.locationSource.data.repository.remote.GoogleMapDemoApiServices
import com.rf.locationSource.data.repository.request.PlaceDetailsResponse
import com.rf.locationSource.data.repository.request.PlacesAutocompleteResponse
import com.rf.locationSource.localDB.GoogleMapDemoDB
import com.rf.locationSource.localDB.model.Place
import com.rf.locationSource.ui.base.BaseRepository
import com.rf.locationSource.utils.SharedPreference
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject

class GoogleMapLocationRepository @Inject constructor(
    val apiServices: GoogleMapDemoApiServices,
    @ApplicationContext val context: Context,
    val sharedPreference: SharedPreference,
    val googleMapDemoDB: GoogleMapDemoDB
) : BaseRepository() {

    fun getPlaceSuggestions(
        success: (placesAutocompleteResponse : PlacesAutocompleteResponse) -> Unit,
        fail: (error: String) -> Unit,
        searchKeyBoard: String,
        message: (msg: String) -> Unit
    ) {

        apiServices.getPlaceSuggestions(searchKeyBoard).apply {
            execute1(this, success, fail, context, message)
        }

    }

    fun getPlaceDetails(
        success: (placesAutocompleteResponse : PlaceDetailsResponse) -> Unit,
        fail: (error: String) -> Unit,
        searchKeyBoard: String,
        message: (msg: String) -> Unit
    ) {

        apiServices.getPlaceDetails(searchKeyBoard).apply {
            execute1(this, success, fail, context, message)
        }

    }

    fun insertPlace(place: Place){
        googleMapDemoDB.googleMapDemoDao().insertPlace(place)
    }

    suspend fun getPlaceListFromDb(): List<Place> {
        return googleMapDemoDB.googleMapDemoDao().getAllPlaces()
    }


}