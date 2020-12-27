package com.del.demoinsta.data.remote

/**
 *
 * end points
 *
 *
 */
object Endpoints {

    // base url
    const val BASE_URL = "https://api.projects.bootcamp.mindorks.com/batch/green/"

    //
    const val HEADER_API_KEY_VALUE = "HBNA19QVVAPP"

    /**
     * Login
     */
    const val LOGIN = "login/mindorks"

    /**
     * GET POST LIST
     *
     * two variants
     *
     * first time - don't need any param
     *
     * second time - need first and last id as params
     *
     */
    const val HOME_POSTS_LIST = "instagram/post/list"


    /**
     * LIKE
     */
    const val POST_LIKE = "instagram/post/like"

    /**
     * UNLIKE
     */
    const val POST_UNLIKE = "instagram/post/unlike"

    //
    const val UPLOAD_IMAGE = "image"

    //
    const val CREATE_POST = "instagram/post"
}