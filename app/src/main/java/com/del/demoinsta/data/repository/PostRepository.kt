package com.del.demoinsta.data.repository


import android.annotation.SuppressLint
import android.util.Log
import com.del.demoinsta.data.model.Post
import com.del.demoinsta.data.model.User
import com.del.demoinsta.data.remote.NetworkService
import com.del.demoinsta.data.remote.Networking
import com.del.demoinsta.data.remote.request.PostCreationRequest
import com.del.demoinsta.data.remote.request.PostLikeModifyRequest
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


/**
 * Repository for Post related.
 *
 *
 * Not singleton and ctor injection.

 *
 *
 * getPostList
 * likePost
 * unlikePost
 *
 * // HomeVM
 *
 * createPost
 *
 * // PhotoVM
 *
 *
 */
@SuppressLint("LogNotTimber")
class PostRepository @Inject constructor(
    //
    private val networkService: NetworkService,
) {

    private val TAG = PostRepository::class.simpleName + " : ";


    /**
     *
     *
     * @param user [User] : Login
     */
    fun fetchHomePostList(
        firstPostId: String?,
        lastPostId: String?,
        user: User
    ): Single<List<Post>> {
        Log.d(TAG, "fetchHomePostList: () " + Networking.HEADER_API_KEY_VALUE)

        //
        return networkService.doHomePostListCall(
            firstPostId,
            lastPostId,
            Networking.HEADER_API_KEY_VALUE,
            user.accessToken,
            user.id
            // Response is PostListResponse
            // and we need data
        ).map {

            //
            it.data
        }
    }

    /**
     *
     * Target Post
     *
     * User (saved in SP/Login)
     *
     *
     *@param post : Target Post
     *@param user [User] : Login
     */
    fun makeLikePost(post: Post, user: User): Single<Post> {
        Log.d(TAG, "makeLikePost: () ")

        //
        return networkService.doPostLikeCall(
            PostLikeModifyRequest(post.id),
            Networking.HEADER_API_KEY_VALUE,
            user.accessToken,
            user.id

            //
        ).map {

            //
            post.likedBy?.apply {

                //
                this.find { postUser -> postUser.id == user.id } ?: this.add(
                    Post.User(
                        user.id,
                        user.name,
                        user.profilePicUrl
                    )
                )
            }
            //
            return@map post
        }
    }


    /**
     *
     * Target Post
     *
     * User (saved in SP/Login)
     *
     *
     *@param post : Target Post
     *@param user [User] : Login
     */
    fun makeUnlikePost(post: Post, user: User): Single<Post> {
        Log.d(TAG, "makeUnlikePost: () ")

        //
        return networkService.doPostUnlikeCall(
            PostLikeModifyRequest(post.id),
            Networking.HEADER_API_KEY_VALUE,
            user.accessToken,
            user.id
        ).map {
            post.likedBy?.apply {
                this.find { postUser -> postUser.id == user.id }?.let { this.remove(it) }
            }
            return@map post
        }
    }


    /**
     *
     */
    fun createPost(imgUrl: String, imgWidth: Int, imgHeight: Int, user: User): Single<Post> {
        Log.d(TAG, "createPost: () ")

        return networkService.doPostCreationCall(
            PostCreationRequest(imgUrl, imgWidth, imgHeight), user.id, user.accessToken
        )
            //
            .map {

                //
                Post(
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


}