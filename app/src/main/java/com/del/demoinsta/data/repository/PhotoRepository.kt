package com.del.demoinsta.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.del.demoinsta.data.model.User
import com.del.demoinsta.data.remote.NetworkService
import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * Repository for image upload
 *
 * Not singleton and ctor injection.
 *
 */
@SuppressLint("LogNotTimber")
class PhotoRepository @Inject constructor(private val networkService: NetworkService) {

    //
    private val TAG = PhotoRepository::class.simpleName + " : ";


    /**
     *
     */
    fun uploadPhoto(file: File, user: User): Single<String> {
        Log.d(TAG, "uploadPhoto: () ")

        // creating a Multipart body
        return MultipartBody.Part
            .createFormData(
                "image",
                file.name,
                RequestBody.create(
                    MediaType.parse("image/*"),
                    file
                )
            )
            .run {
                //
                return@run networkService.doImageUpload(
                    this,
                    user.id,
                    user.accessToken
                )
                    //
                    .map { it.data.imageUrl }
            }
    }
}