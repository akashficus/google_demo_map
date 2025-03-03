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

    @GET("/maps/api/place/autocomplete/json")
    fun getPlaceSuggestions(
        @Query("input") input: String,
        @Query("key") apiKey: String = BuildConfig.gmpApiKey,
        @Query("types") types: String = "geocode",
        @Query("components") components: String = "country:in" // Optional: Restrict to a country
    ): Call<PlacesAutocompleteResponse>

    @GET("/maps/api/place/details/json")
    fun getPlaceDetails(
        @Query("place_id") placeId: String,
        @Query("key") apiKey: String = BuildConfig.gmpApiKey,
    ): Call<PlaceDetailsResponse>

    @GET("maps/api/directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("waypoints") waypoints: String?, // Accepts multiple waypoints
        @Query("key") apiKey: String = BuildConfig.gmpApiKey
    ): Call<DirectionsResponse>
}