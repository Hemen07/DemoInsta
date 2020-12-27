package com.del.demoinsta.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.del.demoinsta.data.model.Post
import com.del.demoinsta.ui.base.adapter.BaseAdapter
import com.del.demoinsta.ui.itemviewholder.PostItemViewHolder

/**
 *
 */
@SuppressLint("LogNotTimber")
class PostsAdapter(
    //
    parentLifecycle: Lifecycle,
    //
    posts: ArrayList<Post>
//
) : BaseAdapter<Post, PostItemViewHolder>(parentLifecycle, posts) {

    //
    private val TAG = PostsAdapter::class.simpleName + " : ";

    //
    init {
        Log.i(TAG, "init()")
    }

    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        // Log.d(TAG, "onCreateViewHolder: () ")
        return PostItemViewHolder(parent)
    }

}