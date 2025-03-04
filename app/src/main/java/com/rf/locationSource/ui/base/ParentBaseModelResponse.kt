package com.rf.locationSource.ui.base
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
open class ParentBaseModelResponse(
    @field:SerializedName("message")
    @Expose
    val message: String? = null,

    @field:SerializedName("status")
    @Expose
    val status: Any? = null,

    @field:SerializedName("error")
    @Expose
    val error: String? = null

)