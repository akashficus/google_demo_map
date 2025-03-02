package com.rf.locationSource.utils

import android.location.Location
import com.rf.locationSource.localDB.model.Place

object Constant {
    const val KEY_INVALID_ACCESS_TOKEN = "Invalid Token"
    const val KEY_SOMETHING_WENT_WRONG = "Something Went Wrong !!"
    const val KEY_SUCCESS = "Success"

    fun calculateDistance(placeStart: Place, placeEnd:Place): Float {
        val startPoint = Location(placeStart.placeName).apply {
            latitude = placeStart.placeLatitude!!
            longitude = placeStart.placeLongitude!!
        }

        val endPoint = Location(placeEnd.placeName).apply {
            latitude = placeEnd.placeLatitude!!
            longitude = placeEnd.placeLongitude!!
        }

        return startPoint.distanceTo(endPoint)
    }
}
enum class APIResponseCode(val codeValue: Int) {
    ResponseCode100(100), ResponseCode101(101), ResponseCode104(104), ResponseCode200(200), ResponseCode201(
        201
    ),
    ResponseCode400(400), ResponseCode401(401), ResponseCode403(403), ResponseCode404(404), ResponseCode500(
        500
    ),
    ResponseCode429(429),
}