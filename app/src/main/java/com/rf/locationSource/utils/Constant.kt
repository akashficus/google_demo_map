package com.rf.locationSource.utils

object Constant {
    const val KEY_INVALID_ACCESS_TOKEN = "Invalid Token"
    const val KEY_SOMETHING_WENT_WRONG = "Something Went Wrong !!"
    const val KEY_SUCCESS = "Success"
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