package com.aflahu.podstream.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.aflahu.podstream.model.Episode
import com.aflahu.podstream.model.Podcast
import com.aflahu.podstream.repository.PodcastRepo
import com.aflahu.podstream.util.DateUtils
import com.aflahu.podstream.viewmodel.SearchViewModel.PodcastSummaryViewData
import java.util.*

class PodcastViewModel(application: Application) : AndroidViewModel(application) {
    var podcastRepo: PodcastRepo? = null
    var activePodcastViewData: PodcastViewData? = null
    private var activePodcast: Podcast? = null
    var livePodcastData: LiveData<List<PodcastSummaryViewData>>? = null

    fun getPodcast(
        podcastSummaryViewData: PodcastSummaryViewData,
        callback: (PodcastViewData?) -> Unit
    ) {
        val repo = podcastRepo ?: return
        val feedUrl = podcastSummaryViewData.feedUrl ?: return

        repo.getPodcast(feedUrl) {
            it?.let {
                it.feedTitle = podcastSummaryViewData.name ?: ""
                it.imageUrl = podcastSummaryViewData.imageUrl ?: ""
                activePodcastViewData = podcastToPodcastView(it)
                activePodcast = it
                callback(activePodcastViewData)
            }
        }
    }

    fun getPodcasts(): LiveData<List<PodcastSummaryViewData>>? {
        val repo = podcastRepo ?: return null

        if (livePodcastData == null) {
            val liveData = repo.getAll()

            livePodcastData = Transformations.map(liveData) { podcastList ->
                podcastList.map { podcast ->
                    podcastToSummaryView(podcast)
                }

            }
        }

        return livePodcastData
    }

    fun saveActivePodcast() {
        val repo = podcastRepo ?: return
        activePodcast?.let { repo.save(it) }
    }

    fun deleteActivePodcast() {
        val repo = podcastRepo ?: return
        activePodcast?.let {
            repo.delete(it)
        }
    }

    private fun podcastToSummaryView(podcast: Podcast): PodcastSummaryViewData {
        return PodcastSummaryViewData(
            podcast.feedTitle,
            DateUtils.dateToShortDate(podcast.lastUpdated),
            podcast.imageUrl,
            podcast.feedUrl
        )
    }

    private fun podcastToPodcastView(podcast: Podcast): PodcastViewData {
        return PodcastViewData(
            podcast.id != null,
            podcast.feedTitle,
            podcast.feedUrl,
            podcast.feedDesc,
            podcast.imageUrl,
            episodeToEpisodesView(podcast.episodes)
        )

    }

    private fun episodeToEpisodesView(episodes: List<Episode>): List<EpisodeViewData> {
        return episodes.map {
            EpisodeViewData(
                it.guid,
                it.title,
                it.description,
                it.mediaUrl,
                it.releaseDate,
                it.duration
            )
        }
    }

    data class PodcastViewData(
        var subscribed: Boolean = false,
        var feedTitle: String? = "",
        var feedUrl: String? = "",
        var feedDesc: String? = "",
        var imageUrl: String? = "",
        var episodes: List<EpisodeViewData>
    )

    data class EpisodeViewData(
        var guid: String? = "",
        var title: String? = "",
        var description: String? = "",
        var mediaUrl: String? = "",
        var releaseDate: Date? = null,
        var duration: String? = ""
    )
}