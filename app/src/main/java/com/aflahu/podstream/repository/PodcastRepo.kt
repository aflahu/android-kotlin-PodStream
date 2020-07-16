package com.aflahu.podstream.repository

import androidx.lifecycle.LiveData
import com.aflahu.podstream.db.PodcastDao
import com.aflahu.podstream.model.Episode
import com.aflahu.podstream.model.Podcast
import com.aflahu.podstream.service.FeedService
import com.aflahu.podstream.service.RssFeedResponse
import com.aflahu.podstream.service.RssFeedService
import com.aflahu.podstream.util.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PodcastRepo(private var feedService: FeedService, private var podcastDao: PodcastDao) {
    fun getPodcast(feedUrl: String, callback: (Podcast?) -> Unit) {
        GlobalScope.launch {
            val podcast = podcastDao.loadPodcast(feedUrl)

            if (podcast != null) {
                podcast.id?.let {
                    podcast.episodes = podcastDao.loadEpisodes(it)
                    GlobalScope.launch(Dispatchers.Main) {
                        callback(podcast)
                    }
                }
            } else {

                val rssFeedService = RssFeedService()

                rssFeedService.getFeed(feedUrl) { feedResponse ->
                    var podcast: Podcast? = null
                    if (feedResponse != null) {
                        podcast = rssResponseToPodcast(feedUrl, "", feedResponse)
                    }
                    GlobalScope.launch(Dispatchers.Main) {
                        callback(podcast)
                    }
                }
            }
        }
    }

    fun getNewEpisodes(localPodcast: Podcast, callback: (List<Episode>) -> Unit) {
        // 1
        feedService.getFeed(localPodcast.feedUrl) { response ->
            if (response != null) {
                // 2
                val remotePodcast =
                    rssResponseToPodcast(localPodcast.feedUrl, localPodcast.imageUrl, response)
                remotePodcast?.let {
                    // 3
                    val localEpisodes = podcastDao.loadEpisodes(localPodcast.id!!)
                    // 4
                    val newEpisodes = remotePodcast.episodes.filter { episode ->
                        localEpisodes.find {
                            episode.guid == it.guid
                        } == null
                    }
                    // 5
                    callback(newEpisodes)
                }
            } else {
                callback(listOf())
            }
        }
    }

    fun getAll(): LiveData<List<Podcast>> {
        return podcastDao.loadPodcasts()
    }

    fun save(podcast: Podcast) {
        GlobalScope.launch {
            val podcastId = podcastDao.insertPodcast(podcast)

            for (episode in podcast.episodes) {
                episode.podcastId = podcastId
                podcastDao.insertEpisode(episode)
            }
        }
    }

    private fun saveNewEpisodes(podcastId: Long, episodes: List<Episode>) {
        GlobalScope.launch {
            for (episode in episodes) {
                episode.podcastId = podcastId
                podcastDao.insertEpisode(episode)
            }
        }
    }

    fun delete(podcast: Podcast) {
        GlobalScope.launch {
            podcastDao.deletePodcast(podcast)
        }
    }

    fun updatePodcastEpisodes(callback: (List<PodcastUpdateInfo>) -> Unit) {
        // 1
        val updatedPodcasts: MutableList<PodcastUpdateInfo> = mutableListOf()
        // 2
        val podcasts = podcastDao.loadPodcastStatic()
        // 3
        var processCount = podcasts.count()
        // 4
        for (podcast in podcasts) {
            // 5
            getNewEpisodes(podcast) {newEpisodes ->
                if (newEpisodes.count() > 0){
                    saveNewEpisodes(podcast.id!!, newEpisodes)
                    updatedPodcasts.add(PodcastUpdateInfo(podcast.feedUrl, podcast.feedTitle, newEpisodes.count()))
                }
                // 7
                processCount--
                if (processCount == 0) {
                    callback(updatedPodcasts)
                }
            }
        }
    }

    private fun rssResponseToPodcast(
        feedUrl: String,
        imageUrl: String,
        rssResponse: RssFeedResponse
    ): Podcast? {
        val items = rssResponse.episodes ?: return null

        val description = if (rssResponse.description == "")
            rssResponse.summary else rssResponse.description

        return Podcast(
            null,
            feedUrl,
            rssResponse.title,
            description,
            imageUrl,
            rssResponse.lastUpdated,
            episodes = rssItemsToEpisodes(items)
        )
    }

    private fun rssItemsToEpisodes(episodeResponses: List<RssFeedResponse.EpisodeResponse>): List<Episode> {
        return episodeResponses.map {
            Episode(
                it.guid ?: "",
                null,
                it.title ?: "",
                it.description ?: "",
                it.url ?: "",
                it.type ?: "",
                DateUtils.xmlDateToDate(it.pubDate),
                it.duration ?: ""
            )
        }
    }

    class PodcastUpdateInfo(val feedUrl: String, val name: String, val newCount: Int)
}