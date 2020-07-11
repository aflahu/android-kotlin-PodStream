package com.aflahu.podstream.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aflahu.podstream.R
import com.aflahu.podstream.viewmodel.SearchViewModel.PodcastSummaryViewData
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.search_item.view.*

class PodcastListAdapter(

    private var podcastSummaryViewList: List<PodcastSummaryViewData>?,
    private val podcastListAdapterListener: PodcastListAdapterListener,
    private val parentActivity: Activity

) : RecyclerView.Adapter<PodcastListAdapter.ViewHolder>() {

    interface PodcastListAdapterListener {
        fun onShowDetails(podcastSummaryViewData: PodcastSummaryViewData)
    }


    inner class ViewHolder(
        v: View,
        private val podcastListAdapterListener: PodcastListAdapterListener
    ) : RecyclerView.ViewHolder(v) {
        var podcastSummaryViewData: PodcastSummaryViewData? = null
        val nameTextView: TextView = v.podcastNameTextView
        val lastUpdatedTextView: TextView = v.podcastLastUpdatedTextView
        val podcastViewImageView: ImageView = v.podcastImage

        init {
            v.setOnClickListener {
                podcastSummaryViewData?.let {
                    podcastListAdapterListener.onShowDetails(it)
                }
            }
        }
    }

    fun setSearchData(podcastSummaryViewData: List<PodcastSummaryViewData>) {
        podcastSummaryViewList = podcastSummaryViewData
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false),
            podcastListAdapterListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchViewList = podcastSummaryViewList ?: return
        val searchView = searchViewList[position]

        holder.podcastSummaryViewData = searchView
        holder.nameTextView.text = searchView.name
        holder.lastUpdatedTextView.text = searchView.lastUpdated
        Glide.with(parentActivity).load(searchView.imageUrl).into(holder.podcastViewImageView)

    }

    override fun getItemCount(): Int {
        return podcastSummaryViewList?.size ?: 0
    }

}


