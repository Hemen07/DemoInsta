package com.del.demoinsta.data.repository

import android.util.Log
import com.del.demoinsta.data.model.Post
import com.del.demoinsta.data.model.User
import com.del.demoinsta.data.remote.NetworkService
import com.del.demoinsta.data.remote.Networking
import com.del.demoinsta.data.remote.request.PostCreationRequest
import com.del.demoinsta.data.remote.request.PostLikeModifyRequest
import com.del.demoinsta.data.remote.response.GeneralResponse
import com.del.demoinsta.data.remote.response.PostListResponse
import io.reactivex.rxjava3.core.Single
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import java.util.*

/**
 *
 */

@RunWith(RobolectricTestRunner::class)
@Config(shadows = [ShadowLog::class])
class PostRepositoryTest {

    /**
     * initialized for @Mock annotated things.
     */
    @Rule
    @JvmField
    var mockitoRule: MockitoRule = MockitoJUnit.rule()


    // param mocked
    @Mock
    private lateinit var networkService: NetworkService


    //target
    private lateinit var postRepository: PostRepository

    //
    companion object {

        //
        private val TAG = PostRepositoryTest::class.java.simpleName + " : "

        //
        @BeforeClass
        @kotlin.jvm.JvmStatic
        fun beforeCLass() {
            println("|||||| beforeCLass ||||||||")
            //
            ShadowLog.setupLogging()
        }

        //
        @AfterClass
        @JvmStatic
        fun afterClass() {
            println("------- afterClass ---------- ")
        }
    }


    @Before
    fun setup() {

        //
        postRepository = PostRepository(networkService)
    }

    @After
    fun tearDown() {

    }

    //
    @Test
    fun fetchHomePostList_requestDoHomePostListCall() {
        Log.d(TAG, "fetchHomePostList_requestDoHomePostListCall: () ")

        // value can be anything.
        val headerApiKeyValue = "HBNA19QVVAPP"

        // we need to set, as original code, we have Networking.HEADER_API_KEY_VALUE
        Networking.HEADER_API_KEY_VALUE = headerApiKeyValue

        // fake user, as we need accessToken, id.
        val user = User("userId", "userName", "userEmail", "accessToken", "profilePicUrl")

        // so, since ns is mocked, so
        // when you hit networkService.doHomePostListCall, you need to tell the return type you are expecting.
        // else you may get default value.

        Mockito.doReturn(
            // actual response
            Single.just(PostListResponse("statusCode", "message", listOf()))
        )
            .`when`(networkService)
            .doHomePostListCall(
                "firstPostId",
                "lastPostId",
                Networking.HEADER_API_KEY_VALUE,
                user.accessToken,
                user.id,
            )


        // now call the method
        postRepository.fetchHomePostList("firstPostId", "lastPostId", user)

        // now verify.
        Mockito.verify(networkService).doHomePostListCall(
            "firstPostId",
            "lastPostId",
            headerApiKeyValue,
            user.accessToken,
            user.id
        )
    }

    @Test
    fun makeLikePost_requestDoMakeLikePost() {
        Log.d(TAG, "makeLikePost_requestDoMakeLikePost: () ")


        // value can be anything.
        val headerApiKeyValue = "HBNA19QVVAPP"

        // we need to set, as original code, we have Networking.HEADER_API_KEY_VALUE
        Networking.HEADER_API_KEY_VALUE = headerApiKeyValue

        // fake user, as we need accessToken, id.
        val user = User("userId", "userName", "userEmail", "accessToken", "profilePicUrl")

        //
        val postUser = Post.User("postUserId", "postUserName", "PostUserUrl")
        val post = Post("postId", "image_url", 100, 100, postUser, mutableListOf(), Date())


        Mockito.doReturn(
            // actual response
            Single.just(GeneralResponse("statusCode", "message"))
        )
            .`when`(networkService)
            .doPostLikeCall(
                PostLikeModifyRequest(post.id),
                Networking.HEADER_API_KEY_VALUE,
                user.accessToken,
                user.id
            )

        //
        postRepository.makeLikePost(post, user)

        // now verify.
        Mockito.verify(networkService).doPostLikeCall(
            PostLikeModifyRequest(post.id),
            Networking.HEADER_API_KEY_VALUE,
            user.accessToken,
            user.id
        )


    }

    @Test
    fun makeUnlikePost_requestDoMakeUnlikePost() {
        Log.d(TAG, "makeUnlikePost_requestDoMakeUnlikePost: () ")


        // value can be anything.
        val headerApiKeyValue = "HBNA19QVVAPP"

        // we need to set, as original code, we have Networking.HEADER_API_KEY_VALUE
        Networking.HEADER_API_KEY_VALUE = headerApiKeyValue

        // fake user, as we need accessToken, id.
        val user = User("userId", "userName", "userEmail", "accessToken", "profilePicUrl")

        //
        val postUser = Post.User("postUserId", "postUserName", "PostUserUrl")
        val post = Post("postId", "image_url", 100, 100, postUser, mutableListOf(), Date())


        Mockito.doReturn(
            // actual response
            Single.just(GeneralResponse("statusCode", "message"))
        )
            .`when`(networkService)
            .doPostUnlikeCall(
                PostLikeModifyRequest(post.id),
                Networking.HEADER_API_KEY_VALUE,
                user.accessToken,
                user.id
            )

        //
        postRepository.makeUnlikePost(post, user)

        // now verify.
        Mockito.verify(networkService).doPostUnlikeCall(
            PostLikeModifyRequest(post.id),
            Networking.HEADER_API_KEY_VALUE,
            user.accessToken,
            user.id
        )


    }

    @Test
    fun makeCreatePost_requestDoPostCreationCall() {
        Log.d(TAG, "makeCreatePost_requestDoPostCreationCall: () ")


        // value can be anything.
        val headerApiKeyValue = "HBNA19QVVAPP"

        // we need to set, as original code, we have Networking.HEADER_API_KEY_VALUE
        Networking.HEADER_API_KEY_VALUE = headerApiKeyValue

        // fake user, as we need accessToken, id.
        val user = User("userId", "userName", "userEmail", "accessToken", "profilePicUrl")

        //
        val postUser = Post.User("postUserId", "postUserName", "PostUserUrl")
        val post = Post("postId", "image_url", 100, 100, postUser, mutableListOf(), Date())


        Mockito.doReturn(
            // actual response
            Single.just(GeneralResponse("statusCode", "message"))
        )
            .`when`(networkService)
            .doPostCreationCall(
                PostCreationRequest("image_url", 100, 100),
                user.id,
                user.accessToken,
                Networking.HEADER_API_KEY_VALUE
            )

        //
        postRepository.createPost("image_url", 100, 100, user)

        // now verify.
        Mockito.verify(networkService).doPostCreationCall(
            PostCreationRequest("image_url", 100, 100),
            user.id,
            user.accessToken,
            Networking.HEADER_API_KEY_VALUE
        )


    }
}