package com.rf.locationSource.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.google.gson.JsonObject
import com.rf.locationSource.data.repository.remote.GoogleMapDemoApiServices
import com.rf.locationSource.data.repository.request.DirectionsResponse
import com.rf.locationSource.data.repository.request.PlaceDetailsResponse
import com.rf.locationSource.data.repository.request.PlacesAutocompleteResponse
import com.rf.locationSource.localDB.GoogleMapDemoDB
import com.rf.locationSource.localDB.model.Place
import com.rf.locationSource.ui.base.BaseRepository
import com.rf.locationSource.utils.SharedPreference
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject
/**
 * Repository class for managing Google Maps API requests and local database operations.
 * Uses dependency injection for API services, shared preferences, and local database access.
 */
class GoogleMapLocationRepository @Inject constructor(
    val apiServices: GoogleMapDemoApiServices, // API service for fetching Google Maps data
    @ApplicationContext val context: Context, // Application context for resource access
    val sharedPreference: SharedPreference, // Shared preferences for storing app data
    val googleMapDemoDB: GoogleMapDemoDB // Local database for storing place information
) : BaseRepository() {

    /**
     * Fetches place suggestions based on the user’s search input.
     * @param success Callback for successful response containing place suggestions.
     * @param fail Callback for error messages.
     * @param searchKeyBoard User input for place search.
     * @param message Callback for additional messages or status updates.
     */
    fun getPlaceSuggestions(
        success: (placesAutocompleteResponse: PlacesAutocompleteResponse) -> Unit,
        fail: (error: String) -> Unit,
        searchKeyBoard: String,
        message: (msg: String) -> Unit
    ) {
        apiServices.getPlaceSuggestions(searchKeyBoard).apply {
            execute1(this, success, fail, context, message) // Executes the API call and handles response
        }
    }

    /**
     * Fetches detailed information about a place using its place ID.
     * @param success Callback for successful response containing place details.
     * @param fail Callback for error messages.
     * @param searchKeyBoard Place ID to fetch details.
     * @param message Callback for additional messages or status updates.
     */
    fun getPlaceDetails(
        success: (placesAutocompleteResponse: PlaceDetailsResponse) -> Unit,
        fail: (error: String) -> Unit,
        searchKeyBoard: String,
        message: (msg: String) -> Unit
    ) {
        apiServices.getPlaceDetails(searchKeyBoard).apply {
            execute1(this, success, fail, context, message) // Executes the API call and handles response
        }
    }

    /**
     * Fetches directions between two locations with optional waypoints.
     * @param success Callback for successful response containing directions.
     * @param fail Callback for error messages.
     * @param origin Starting location in "latitude,longitude" format.
     * @param destination Destination location in "latitude,longitude" format.
     * @param waypoints Optional intermediate waypoints (comma-separated list).
     * @param message Callback for additional messages or status updates.
     */
    fun getDirections(
        success: (placesAutocompleteResponse: DirectionsResponse) -> Unit,
        fail: (error: String) -> Unit,
        origin: String,
        destination: String,
        waypoints: String,
        message: (msg: String) -> Unit
    ) {
        apiServices.getDirections(origin, destination, waypoints).apply {
            execute1(this, success, fail, context, message) // Executes the API call and handles response
        }
    }

    /**
     * Inserts a place into the local database.
     * @param place Place object to insert into the database.
     */
    fun insertPlace(place: Place) {
        googleMapDemoDB.googleMapDemoDao().insertPlace(place)
    }

    /**
     * Retrieves a list of stored places from the local database.
     * @return List of places stored in the database.
     */
    suspend fun getPlaceListFromDb(): List<Place> {
        return googleMapDemoDB.googleMapDemoDao().getAllPlaces()
    }
}