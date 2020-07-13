package com.aflahu.podstream.db

import android.content.Context
import androidx.room.*
import com.aflahu.podstream.model.Episode
import com.aflahu.podstream.model.Podcast
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return (date?.time)
    }
}

@Database(entities = arrayOf(Podcast::class, Episode::class), version = 1)
@TypeConverters(Converters::class)
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