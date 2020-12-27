package com.del.demoinsta.data.model

/**
 *
 * Wrapper
 *
 * contains attributes of an Image that to be displayed
 *
 * url
 * header (for api)
 * placeholder width and height
 * // given, during tome of load image image will jump/stretch kind of
 * // since no height and width provided.
 *
 * GOT IT
 *
 */
data class Image(
    //
    val url: String,
    //
    val headers: Map<String, String>,
    //
    val placeholderWidth: Int = -1,
    //
    val placeholderHeight: Int = -1
)