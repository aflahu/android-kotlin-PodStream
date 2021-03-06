package com.aflahu.podstream.viewmodel

import android.app.Application
import android.app.SharedElementCallback
import androidx.lifecycle.AndroidViewModel
import com.aflahu.podstream.repository.ItunesRepo
import com.aflahu.podstream.service.PodcastResponse
import com.aflahu.podstream.util.DateUtils

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    var itunesRepo: ItunesRepo? = null

    data class PodcastSummaryViewData(
        var name: String? = "",
        var lastUpdated: String? = "",
        var imageUrl: String? = "",
        var feedUrl: String? = ""
    )

    private fun itunesPodcastToPodcastSummaryView(
        itunesPodcast: PodcastResponse.ItunesPodcast
    ): PodcastSummaryViewData {
        return PodcastSummaryViewData(
            itunesPodcast.collectionCensoredName,
            DateUtils.jsonDateToShortDate(itunesPodcast.releaseDate),
            itunesPodcast.artworkUrl30,
            itunesPodcast.feedUrl
        )
    }

    fun searchPodcasts(term: String, callback: (List<PodcastSummaryViewData>) -> Unit) {
        itunesRepo?.searchByTerm(term) { results ->
            if (results == null) {
                callback(emptyList())
            } else {
                val searchViews = results.map { podcast ->
                    itunesPodcastToPodcastSummaryView(podcast)
                }
                callback(searchViews)
            }
        }
    }

}