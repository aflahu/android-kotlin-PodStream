package com.aflahu.podstream.service

import java.util.*

data class RssFeedResponse(
    var title: String = "",
    var description: String = "",
    var summary: String = "",
    var lastUpdated: Date = Date(),
    var episodes: MutableList<EpisodeResponse>? = null
) {
    data class EpisodeResponse(
        var title: String = "",
        var link: String = "",
        var description: String = "",
        var guid: String = "",
        var pubDate: String = "",
        var duration: String = "",
        var url: String = "",
        var type: String = ""
    )
}