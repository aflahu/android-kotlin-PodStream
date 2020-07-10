package com.aflahu.podstream.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.aflahu.podstream.R
import com.aflahu.podstream.repository.ItunesRepo
import com.aflahu.podstream.service.ItunesService

class PodcastActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcast)


        val itunesService = ItunesService.instance

        val itunesRepo = ItunesRepo(itunesService)

        itunesRepo.searchByTerm("Android Developer") {
            Log.i(TAG, "Results = $it")
        }
    }
}