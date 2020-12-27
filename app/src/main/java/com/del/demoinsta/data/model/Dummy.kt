package com.del.demoinsta.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 *
 * TODO : Not used, will be removed.
 */
data class Dummy(
    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("imageUrl")
    val imageUrl: String?
)