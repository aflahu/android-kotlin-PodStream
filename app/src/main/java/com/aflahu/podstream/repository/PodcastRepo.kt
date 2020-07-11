package com.aflahu.podstream.repository

import com.aflahu.podstream.model.Podcast
import javax.security.auth.callback.Callback

class PodcastRepo {
    fun getPodcast(feedUrl: String, callback: (Podcast?) -> Unit) {
        callback(
            Podcast(feedUrl, "No Name", "No description", "No image")
        )
    }
}