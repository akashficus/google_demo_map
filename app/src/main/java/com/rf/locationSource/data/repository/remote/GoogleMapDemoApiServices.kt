package com.rf.locationSource.data.repository.remote

import com.google.gson.JsonObject
import com.rf.locationSource.BuildConfig
import com.rf.locationSource.data.repository.request.DirectionsResponse
import com.rf.locationSource.data.repository.request.PlaceDetailsResponse
import com.rf.locationSource.data.repository.request.PlacesAutocompleteResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapDemoApiServices {

    /**
     * Fetches place suggestions based on user input.
     * @param input The search query typed by the user.
     * @param apiKey API key for authentication (default: from BuildConfig).
     * @param types Restricts results to a specific type (default: geocode for addresses).
     * @param components Restricts results to a specific country (default: India).
     * @return A [Call] object for the PlacesAutocompleteResponse.
     */
    @GET("/maps/api/place/autocomplete/json")
    fun getPlaceSuggestions(
        @Query("input") input: String,
        @Query("key") apiKey: String = BuildConfig.gmpApiKey,
        @Query("types") types: String = "geocode",
        @Query("components") components: String = "country:in" // Restrict to India
    ): Call<PlacesAutocompleteResponse>

    /**
     * Fetches detailed information about a place.
     * @param placeId Unique identifier of the place.
     * @param apiKey API key for authentication (default: from BuildConfig).
     * @return A [Call] object for the PlaceDetailsResponse.
     */
    @GET("/maps/api/place/details/json")
    fun getPlaceDetails(
        @Query("place_id") placeId: String,
        @Query("key") apiKey: String = BuildConfig.gmpApiKey,
    ): Call<PlaceDetailsResponse>

    /**
     * Fetches directions between two locations with optional waypoints.
     * @param origin Starting location in "latitude,longitude" format.
     * @param destination Destination location in "latitude,longitude" format.
     * @param waypoints Optional intermediate waypoints (comma-separated list).
     * @param apiKey API key for authentication (default: from BuildConfig).
     * @return A [Call] object for the DirectionsResponse.
     */
    @GET("maps/api/directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("waypoints") waypoints: String?, // Supports multiple waypoints
        @Query("key") apiKey: String = BuildConfig.gmpApiKey
    ): Call<DirectionsResponse>
}
