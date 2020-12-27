package com.del.demoinsta.ui.itemviewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.del.demoinsta.R
import com.del.demoinsta.data.model.Image
import com.del.demoinsta.data.model.Post
import com.del.demoinsta.data.remote.Networking
import com.del.demoinsta.data.repository.PostRepository
import com.del.demoinsta.data.repository.UserRepository
import com.del.demoinsta.ui.base.adapter.BaseItemViewModel
import com.del.demoinsta.utils.common.Resource
import com.del.demoinsta.utils.common.TimeUtils
import com.del.demoinsta.utils.display.ScreenUtils
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.rx.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@SuppressLint("LogNotTimber")
class PostItemViewModel @Inject constructor(
    //
    schedulerProvider: SchedulerProvider,
    //
    compositeDisposable: CompositeDisposable,
    //
    networkHelper: NetworkHelper,
    //
    userRepository: UserRepository,
    //
    private val postRepository: PostRepository

) : BaseItemViewModel<Post>(schedulerProvider, compositeDisposable, networkHelper) {

    //
    companion object {
        const val TAG = "PostItemViewModel"
    }

    // hmm,
    override fun notifyOnCreateFinished() {
        Log.d(TAG, "onCreate called")
    }


    //
    private val user = userRepository.getCurrentUser()!!

    // need to screen width and height, so that image can be scaled to fit in each device.
    private val screenWidth = ScreenUtils.getScreenWidth()
    private val screenHeight = ScreenUtils.getScreenHeight()

    // header for image
    private val headers = mapOf(
        Pair(Networking.HEADER_API_KEY, Networking.HEADER_API_KEY_VALUE),
        Pair(Networking.HEADER_USER_ID, user.id),
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken)
    )

    //  using Transformation/Rx map coz everything  is available in  dataMutableLiveData
    // who holds Post object.
    val name: LiveData<String> = Transformations.map(dataMutableLiveData) { it.creator.name }

    //
    val postTime: LiveData<String> =
        Transformations.map(dataMutableLiveData) { TimeUtils.getTimeAgo(it.createdAt) }

    //
    val likesCount: LiveData<Int> =
        Transformations.map(dataMutableLiveData) { it.likedBy?.size ?: 0 }

    // if logged in user present in post.user likeBy list then he liked it before
    val isLiked: LiveData<Boolean> = Transformations.map(dataMutableLiveData) {
        it.likedBy?.find { postUser ->
            postUser.id == user.id
        } !== null

    }

    /**
     *
     */
    val profileImage: LiveData<Image> = Transformations.map(dataMutableLiveData) {
        it.creator.profilePicUrl?.run { Image(this, headers) }
    }

    /**
     * Posted image
     *
     * width same
     * height
     *    //
     */
    val imageDetail: LiveData<Image> = Transformations.map(dataMutableLiveData) {
        Image(
            it.imageUrl,
            headers,
            screenWidth,
            it.imageHeight?.let { height ->
                return@let (calculateScaleFactor(it) * height).toInt()
            } ?: screenHeight / 3)
    }


    /**
     * Like Request
     */
    fun onLikeClick() = dataMutableLiveData.value?.let {
        Log.v(TAG, "onLikeClick: () $it")
        Log.w(TAG, "onLikeClick: () $user")
        //
        if (networkHelper.isNetworkConnected()) {

            // get the Observable
            // since, cold, it won't run away unless someone subscribes to it.
            val api =
                if (isLiked.value == true)
                    postRepository.makeUnlikePost(it, user)
                else
                    postRepository.makeLikePost(it, user)

            compositeDisposable.add(api
                .subscribeOn(schedulerProvider.io())
                .subscribe(

                    { responsePost ->
                        Log.i(TAG, "onLikeClick: success() ")
                        //
                        if (responsePost.id == it.id) bindData(responsePost)

                    },

                    { error ->

                        Log.w(TAG, "onLikeClick: error() ")
                        //
                        handleNetworkError(error)

                    }
                )
            )
        } else {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
        }
    }


    /**
     *
     */
    private fun calculateScaleFactor(post: Post) =
        post.imageWidth?.let { return@let screenWidth.toFloat() / it } ?: 1f

}