package com.del.demoinsta.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 *
 *
 * Used in POST LIKE and POST UNLIKE
 */
data class GeneralResponse(
    @Expose
    @SerializedName("statusCode")
    var statusCode: String,

    @Expose
    @SerializedName("message")
    var message: String
)