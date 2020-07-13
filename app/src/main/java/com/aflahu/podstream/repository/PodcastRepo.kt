package com.aflahu.podstream.repository

import com.aflahu.podstream.model.Podcast
import com.aflahu.podstream.service.RssFeedService
import javax.security.auth.callback.Callback

class PodcastRepo {
    fun getPodcast(feedUrl: String, callback: (Podcast?) -> Unit) {
        val rssFeedService = RssFeedService()

        rssFeedService.getFeed(feedUrl) {}

        callback(
            Podcast(feedUrl, "No Name", "No description", "No image")
        )
    }
}