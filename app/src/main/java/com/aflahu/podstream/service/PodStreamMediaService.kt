package com.aflahu.podstream.service

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat

class PodStreamMediaService : MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat

    override fun onCreate() {
        super.onCreate()
        createMediaSession()
    }

    private fun createMediaSession() {
        mediaSession = MediaSessionCompat.fromMediaSession(this, "PodstreamMediaService")

        setSessionToken(mediaSession.sessionToken)

        val callback = PodstreamMediaCallback(this, mediaSession)
        mediaSession.setCallback(callback)

    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        TODO("Not yet implemented")
        return null
    }

}