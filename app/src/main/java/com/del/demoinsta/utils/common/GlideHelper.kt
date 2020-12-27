package com.del.demoinsta.utils.common

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

/**
 *
 * Needed header for image url
 *
 *
 */
object GlideHelper {

    fun getProtectedUrl(url: String, headers: Map<String, String>): GlideUrl {
        val builder = LazyHeaders.Builder()
        for (entry in headers) builder.addHeader(entry.key, entry.value)
        return GlideUrl(url, builder.build())
    }
}
