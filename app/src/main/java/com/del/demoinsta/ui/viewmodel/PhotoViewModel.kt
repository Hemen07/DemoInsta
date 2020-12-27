package com.del.demoinsta.ui.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.del.demoinsta.R
import com.del.demoinsta.data.model.Post
import com.del.demoinsta.data.model.User
import com.del.demoinsta.data.repository.PhotoRepository
import com.del.demoinsta.data.repository.PostRepository
import com.del.demoinsta.data.repository.UserRepository
import com.del.demoinsta.ui.base.BaseViewModel
import com.del.demoinsta.utils.common.Event
import com.del.demoinsta.utils.common.FileUtils
import com.del.demoinsta.utils.common.Resource
import com.del.demoinsta.utils.network.NetworkHelper
import com.del.demoinsta.utils.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.io.File
import java.io.InputStream


/**
 *
 *
 */
@SuppressLint("LogNotTimber")
class PhotoViewModel(
    //
    schedulerProvider: SchedulerProvider,
    //
    compositeDisposable: CompositeDisposable,
    //
    networkHelper: NetworkHelper,
    //
    private val userRepository: UserRepository,
    //
    private val photoRepository: PhotoRepository,
    //
    private val postRepository: PostRepository,
    //
    private val directory: File
//
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    //
    private val TAG = PhotoViewModel::class.simpleName + " : ";

    private val user: User =
        userRepository.getCurrentUser()!! // should not be used without logged in user


    //
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    // for post creation  success monitor.
    val post: MutableLiveData<Event<Post>> = MutableLiveData()

    //
    init {
        Log.i(TAG, "init()");
    }


    // you know, why I use this lol.
    override fun notifyOnCreateFinished() {
        Log.d(TAG, "notifyOnCreateFinished: () ")

    }


    /**
     *
     *
     */
    fun onGalleryImageSelected(inputStream: InputStream) {
        Log.d(TAG, "onGalleryImageSelected: () ")

        //
        loading.postValue(true)

        //
        compositeDisposable.add(

            // used Callable
            //
            Single.fromCallable {

                //
                FileUtils.saveInputStreamToFile(
                    inputStream,
                    directory,
                    "gallery_img_temp",
                    500
                )
            }
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        if (it != null) {

                            // once success storing, above
                            // get the width  and height, of an bitmap from file ().
                            FileUtils.getImageSize(it)?.run {

                                //
                                uploadPhotoAndCreatePost(it, this)
                            }
                        } else {
                            loading.postValue(false)
                            messageStringId.postValue(Resource.error(R.string.try_again))

                        }
                    },
                    {
                        loading.postValue(false)
                        messageStringId.postValue(Resource.error(R.string.try_again))
                    }
                )
        )
    }


    /**
     * Supplying  a fn/lambda
     * without params and returns a string
     *
     * lambda containing "camera.cameraBitmapPath()"
     */
    fun onCameraImageTaken(cameraImageProcessor: () -> String) {
        Log.d(TAG, "onCameraImageTaken: () ")

        //
        loading.postValue(true)
        //
        compositeDisposable.add(

            // executing camera.cameraBitmapPath()
            // Again, I could have easily supplied camera instance and call cameraBitmapPath()
            // less coupling and no android specific part here.
            Single.fromCallable { cameraImageProcessor() }
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        File(it).apply {

                            // once success storing, above
                            // get the width  and height, of an bitmap from file ().
                            FileUtils.getImageSize(this)?.let { size ->
                                //
                                uploadPhotoAndCreatePost(this, size)
                            } ?: loading.postValue(false)
                        }
                    },
                    {
                        loading.postValue(false)
                        messageStringId.postValue(Resource.error(R.string.try_again))
                    }
                )
        )
    }


    /**
     *
     *
     *
     *  Uploads a photo (image at this point is resized)
     *
     *  once success, create a post
     *
     *
     *  Two operation : so flatmap
     *
     */
    private fun uploadPhotoAndCreatePost(imageFile: File, imageSize: Pair<Int, Int>) {
//        Logger.d("DEBUG", imageFile.path)
        Log.d(TAG, "uploadPhotoAndCreatePost: () $imageFile.path")

        //
        compositeDisposable.add(

            //
            photoRepository.uploadPhoto(imageFile, user)

                // on success, call this api
                .flatMap {
                    //
                    postRepository.createPost(it, imageSize.first, imageSize.second, user)
                }
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        loading.postValue(false)
                        post.postValue(Event(it))
                    },
                    {
                        handleNetworkError(it)
                        loading.postValue(false)
                    }
                )

        )
    }

    // don't worry not calling onClear, BVM handling that
    // this is just specific to this class.
    fun clear() {
        Log.d(TAG, "clear: () ")
        // not needed
        //  compositeDisposable.clear()
    }
}