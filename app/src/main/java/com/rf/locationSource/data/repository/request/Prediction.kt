package com.rf.locationSource.data.repository.request

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

/**
 * Represents the response received from the Google Places Autocomplete API.
 * Contains a list of place predictions based on user input.
 */
data class Prediction(
    @SerializedName("description") val description: String,
    @Json(name = "place_id") val placeId: String
)