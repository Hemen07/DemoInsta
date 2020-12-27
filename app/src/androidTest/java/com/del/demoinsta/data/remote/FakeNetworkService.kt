package com.del.demoinsta.data.remote

import com.del.demoinsta.data.model.Post
import com.del.demoinsta.data.remote.request.LoginRequest
import com.del.demoinsta.data.remote.request.PostCreationRequest
import com.del.demoinsta.data.remote.request.PostLikeModifyRequest
import com.del.demoinsta.data.remote.response.*
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import java.util.*

/**
 *
 * Required to create fake class of this NS
 * to be used in HF for showing item in RV
 *
 *
 * For now, we only provide implementation for
 * doLoginCall()
 * doHomePostListCall()
 *
 * since only required
 *
 */
class FakeNetworkService : NetworkService {


    override fun doLoginCall(request: LoginRequest, apiKey: String): Single<LoginResponse> {


        return Single.just(
            LoginResponse(

                "statusCode",
                200,
                "success",
                "accessToken",
                "userId",
                "userName",
                "userEmail",
                "profilePicUrl"
            )
        )
    }

    /*
    *
    *
    */
    override fun doHomePostListCall(
        firstPostId: String?,
        lastPostId: String?,
        apiKey: String,
        accessToken: String,
        userId: String
    ): Single<PostListResponse> {

        // some static data for testing.

        //
        val postUser1 = Post.User("postUserId1", "postUserName1", "PostUserUrl1")
        val postUser2 = Post.User("postUserId2", "postUserName2", "PostUserUrl2")

        //
        val likedBy = mutableListOf<Post.User>(
            Post.User("postUserId3", "postUserName3", "PostUserUrl3"),
            Post.User("postUserId4", "postUserName4", "PostUserUrl4")
        )

        //
        val post1 = Post("postId", "image_url", 400, 400, postUser1, likedBy, Date())
        val post2 = Post("postId", "image_url", 400, 400, postUser2, likedBy, Date())

        //
        val postListResponse = PostListResponse("statusCode", "success", listOf(post1, post2))

        //
        return Single.just(postListResponse)

    }

    override fun doPostLikeCall(
        request: PostLikeModifyRequest,
        apiKey: String,
        accessToken: String,
        userId: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }

    override fun doPostUnlikeCall(
        request: PostLikeModifyRequest,
        apiKey: String,
        accessToken: String,
        userId: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }

    override fun doImageUpload(
        imageMultipartBody: MultipartBody.Part,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<ImageResponse> {
        TODO("Not yet implemented")
    }

    override fun doPostCreationCall(
        request: PostCreationRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<PostCreationResponse> {
        TODO("Not yet implemented")
    }
}