package com.del.demoinsta.data.remote

import com.del.demoinsta.data.remote.request.LoginRequest
import com.del.demoinsta.data.remote.request.PostCreationRequest
import com.del.demoinsta.data.remote.request.PostLikeModifyRequest
import com.del.demoinsta.data.remote.response.*
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.*
import javax.inject.Singleton

/**
 * Interface will be used by Retrofit.
 */
@Singleton
interface NetworkService {


    /**
     *  LOGIN
     */
    @POST(Endpoints.LOGIN)
    fun doLoginCall(
        @Body request: LoginRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.HEADER_API_KEY_VALUE
    ): Single<LoginResponse>


    /**
     * GET POST LIST
     *
     * two variants
     *
     *1) first time - don't need any param
     *
     *2) second time - need first and last id as params
     *
     * @return Single Observable of PostListResponse.
     *
     */
    @GET(Endpoints.HOME_POSTS_LIST)
    fun doHomePostListCall(

        @Query("firstPostId") firstPostId: String?,
        @Query("lastPostId") lastPostId: String?,

        //
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.HEADER_API_KEY_VALUE,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_USER_ID) userId: String
    ): Single<PostListResponse>


    /**
     * LIKE POST
     * Body is [PostLikeModifyRequest]
     *
     */
    @PUT(Endpoints.POST_LIKE)
    fun doPostLikeCall(

        // body to include
        @Body request: PostLikeModifyRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.HEADER_API_KEY_VALUE,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_USER_ID) userId: String
    ): Single<GeneralResponse>


    /**
     * POST UNLIKE
     * Body is [PostLikeModifyRequest]
     *
     */
    @PUT(Endpoints.POST_UNLIKE)
    fun doPostUnlikeCall(

        // body to include
        @Body request: PostLikeModifyRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.HEADER_API_KEY_VALUE,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_USER_ID) userId: String
    ): Single<GeneralResponse>


    /**
     *  Image Upload
     */
    @Multipart
    @POST(Endpoints.UPLOAD_IMAGE)
    fun doImageUpload(
        @Part imageMultipartBody: MultipartBody.Part,
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.HEADER_API_KEY_VALUE
    ): Single<ImageResponse>

    /**
     * Post Create
     * Body is [PostCreationRequest]
     */
    @POST(Endpoints.CREATE_POST)
    fun doPostCreationCall(
        @Body request: PostCreationRequest,
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.HEADER_API_KEY_VALUE
    ): Single<PostCreationResponse>
}