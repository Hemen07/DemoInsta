package com.del.demoinsta.utils.common

import android.annotation.SuppressLint
import android.util.Log
import com.del.demoinsta.data.model.Post
import com.del.demoinsta.data.model.User
import com.del.demoinsta.data.remote.response.PostCreationResponse
import com.del.demoinsta.data.remote.response.PostListResponse

object CommonUtil {
    private val TAG = CommonUtil::class.java.simpleName + " : "

    @SuppressLint("LogNotTimber")
    fun getSum(a: Int, b: Int): Int {
        Log.d(TAG, "getSum () :  ")
        return a + b
    }


    //
    fun some(it: PostListResponse): List<Post> {
        return it.data
    }

    fun some2(user: User, it: PostCreationResponse): Post {
        return Post(
            it.data.id,
            it.data.imageUrl,
            it.data.imageWidth,
            it.data.imageHeight,
            // we are creating, so details can be filled from Logged in user.
            Post.User(
                user.id,
                user.name,
                user.profilePicUrl
            ),
            // emptyList likedBy.
            mutableListOf(),
            it.data.createdAt
        )
    }

}