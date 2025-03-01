package com.rf.locationSource.data.repository.request

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class Prediction(
    @SerializedName("description") val description: String,
    @Json(name = "place_id") val placeId: String
)