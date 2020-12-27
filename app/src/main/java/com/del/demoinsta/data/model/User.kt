package com.del.demoinsta.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * User
 * Info retrieved from Login.
 * Store in SP
 */
data class User(

    @Expose
    @SerializedName("userId")
    val id: String,

    @Expose
    @SerializedName("userName")
    val name: String,

    @Expose
    @SerializedName("userEmail")
    val email: String,

    @Expose
    @SerializedName("accessToken")
    val accessToken: String,

    @Expose
    @SerializedName("profilePicUrl")
    val profilePicUrl: String? = null
)