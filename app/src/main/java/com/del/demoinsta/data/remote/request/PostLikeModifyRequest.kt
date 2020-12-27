package com.del.demoinsta.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 *
 * Body for Post LIKE and UNLIKE
 *
 */
data class PostLikeModifyRequest(
    @Expose
    @SerializedName("postId")
    var postId: String
)