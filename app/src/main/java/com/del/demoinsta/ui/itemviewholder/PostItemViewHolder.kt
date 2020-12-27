package com.del.demoinsta.ui.itemviewholder

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.del.demoinsta.R
import com.del.demoinsta.data.model.Post
import com.del.demoinsta.di.component.ViewHolderComponent
import com.del.demoinsta.ui.base.adapter.BaseItemViewHolder
import com.del.demoinsta.ui.itemviewmodel.PostItemViewModel
import com.del.demoinsta.utils.common.GlideHelper


@SuppressLint("LogNotTimber")
class PostItemViewHolder(parent: ViewGroup) :
    BaseItemViewHolder<Post, PostItemViewModel>(R.layout.item_view_post, parent) {

    //
    private val TAG = PostItemViewHolder::class.simpleName + " : ";

    //
    private val LOG_DEBUG = false;

    // views
    //
    private lateinit var mNameTv: TextView

    //
    private lateinit var mProfileImv: ImageView

    //
    private lateinit var mPostImv: ImageView

    //
    private lateinit var mLikePostImv: ImageView

    //
    private lateinit var mLikeCountTv: TextView

    //
    private lateinit var mTimeTv: TextView


    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        if (LOG_DEBUG) Log.d(TAG, "injectDependencies: () ")
        //
        viewHolderComponent.inject(this)
    }

    /*
     *
     */
    override fun setupView(view: View) {
        if (LOG_DEBUG) Log.d(TAG, "setupView: () ")

        //
        mNameTv = view.findViewById(R.id.tvName)
        mProfileImv = view.findViewById(R.id.ivProfile)
        mPostImv = view.findViewById(R.id.ivPost)
        mLikePostImv = view.findViewById(R.id.ivLike)
        mLikeCountTv = view.findViewById(R.id.tvLikesCount)
        mTimeTv = view.findViewById(R.id.tvTime)

        // on like click ->
        mLikePostImv.setOnClickListener {
            Log.i(TAG, "setupView: onClick : Like() ")
            viewModel.onLikeClick()
        }
    }

    /*
     *
     */
    override fun setupObservers() {
        if (LOG_DEBUG) Log.d(TAG, "setupObservers: () ")
        super.setupObservers()

        //
        viewModel.name.observe(this, Observer {
            mNameTv.text = it
        })

        //
        viewModel.postTime.observe(this, Observer {
            mTimeTv.text = it
        })

        //
        viewModel.likesCount.observe(this, Observer {
            mLikeCountTv.text = itemView.context.getString(R.string.post_like_label, it)
        })

        //
        viewModel.isLiked.observe(this, Observer {
            if (it) mLikePostImv.setImageResource(R.drawable.ic_heart_selected)
            else mLikePostImv.setImageResource(R.drawable.ic_heart_unselected)
        })

        // Used a GlideHelper, coz Glide do not use Header
        //
        viewModel.profileImage.observe(this, Observer {
            it?.run {

                val glideRequest = Glide
                    .with(mProfileImv.context)
                    // GlideHelper, returns GlideUrl
                    .load(GlideHelper.getProtectedUrl(url, headers))

                    // circular image transform, could have used hodenhoff imv
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

                if (placeholderWidth > 0 && placeholderHeight > 0) {

                    //

                    val params = mProfileImv.layoutParams as ViewGroup.LayoutParams
                    params.width = placeholderWidth
                    params.height = placeholderHeight
                    mProfileImv.layoutParams = params
                    glideRequest
                        .apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_unselected))
                }

                //
                glideRequest.into(mProfileImv)
            }
        })

        //
        viewModel.imageDetail.observe(this, Observer {
            it?.run {
                val glideRequest = Glide
                    .with(mPostImv.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))

                if (placeholderWidth > 0 && placeholderHeight > 0) {
                    val params = mPostImv.layoutParams as ViewGroup.LayoutParams
                    params.width = placeholderWidth
                    params.height = placeholderHeight
                    mPostImv.layoutParams = params
                    glideRequest
                        .apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_photo))
                }
                glideRequest.into(mPostImv)
            }
        })
    }


}