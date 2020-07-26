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
        if (parentId.equals(PODSTREAM_EMPTY_ROOT_MEDIA_ID)) {
            result.sendResult(null)
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return MediaBrowserServiceCompat.BrowserRoot(PODSTREAM_EMPTY_ROOT_MEDIA_ID, null)
    }

    companion object {
        private const val PODSTREAM_EMPTY_ROOT_MEDIA_ID = "podstream_empty_root_media_id"
    }
}