package com.rf.locationSource.data.repository.request

import com.google.gson.annotations.SerializedName

data class PlacesAutocompleteResponse(
    @SerializedName("predictions") val predictions: List<Prediction>,
    @SerializedName("status") val status: String
)
