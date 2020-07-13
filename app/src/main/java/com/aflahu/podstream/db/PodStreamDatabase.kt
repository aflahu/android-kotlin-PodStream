package com.aflahu.podstream.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aflahu.podstream.model.Episode
import com.aflahu.podstream.model.Podcast

@Database(entities = arrayOf(Podcast::class, Episode::class), version = 1)
abstract class PodStreamDatabase : RoomDatabase() {
    abstract fun podcastDao(): PodcastDao

    companion object {
        private var instance: PodStreamDatabase? = null

        fun getInstance(context: Context): PodStreamDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    PodStreamDatabase::class.java,
                    "PodStream"
                ).build()
            }
            return instance as PodStreamDatabase
        }
    }
}