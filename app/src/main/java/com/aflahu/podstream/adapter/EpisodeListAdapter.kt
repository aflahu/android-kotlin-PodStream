package com.aflahu.podstream.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aflahu.podstream.R
import com.aflahu.podstream.util.DateUtils
import com.aflahu.podstream.util.HtmmlUtils
import com.aflahu.podstream.viewmodel.PodcastViewModel
import kotlinx.android.synthetic.main.episode_item.view.*

class EpisodeListAdapter(
    private var episodeViewList: List<PodcastViewModel.EpisodeViewData>?,
    private val episodeListAdapterListener: EpisodeListAdapterListener
) :
    RecyclerView.Adapter<EpisodeListAdapter.ViewHolder>() {
    class ViewHolder(v: View, private val episodeListAdapterListener: EpisodeListAdapterListener) :
        RecyclerView.ViewHolder(v) {

        init {
            v.setOnClickListener {
                episodeViewData?.let {
                    episodeListAdapterListener.onSelectedEpisode(it)
                }
            }
        }

        var episodeViewData: PodcastViewModel.EpisodeViewData? = null
        val titleTextView: TextView = v.titleView
        val descTextView: TextView = v.descView
        val durationTextView: TextView = v.durationView
        val releaseDateTextView: TextView = v.releaseDateView
    }

    interface EpisodeListAdapterListener {
        fun onSelectedEpisode(episodeViewData: PodcastViewModel.EpisodeViewData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.episode_item, parent, false),
            episodeListAdapterListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episodeViewList = episodeViewList ?: return
        val episodeView = episodeViewList[position]

        holder.episodeViewData = episodeView
        holder.titleTextView.text = episodeView.title
        holder.descTextView.text = HtmmlUtils.htmlToSpannable(episodeView.description ?: "")
        holder.durationTextView.text = episodeView.duration
        holder.releaseDateTextView.text =
            episodeView.releaseDate?.let { DateUtils.dateToShortDate(it) }
    }

    override fun getItemCount(): Int {
        return episodeViewList?.size ?: 0
    }
}