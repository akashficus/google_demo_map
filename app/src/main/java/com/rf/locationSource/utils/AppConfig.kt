package com.rf.locationSource.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class AppConfig @Inject constructor(
    private val context: Context
) {
    var baseUrl: String = "https://maps.googleapis.com"

}