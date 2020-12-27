package com.del.demoinsta.data.remote.response

import com.del.demoinsta.data.model.Post
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 *
 * Response for get All post List
 *
 *HOME_POSTS_LIST
 *
 */
data class PostListResponse(
    @Expose
    @SerializedName("statusCode")
    var statusCode: String,

    @Expose
    @SerializedName("message")
    var message: String,

    @Expose
    @SerializedName("data")
    val data: List<Post>
)