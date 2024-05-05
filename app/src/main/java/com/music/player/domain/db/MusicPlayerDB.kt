package com.music.player.domain.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.music.player.data.model.Track


@Database(version = 1, exportSchema = false, entities = [Track::class])
abstract class MusicPlayerDB : RoomDatabase() {

    abstract fun trackDao() : TrackDao

    companion object {
        @Volatile
        var dbInstance: MusicPlayerDB? = null

        fun getInstance(context: Context): MusicPlayerDB {
            return dbInstance ?: synchronized(this) {
                dbInstance ?: createDatabase(context).also { dbInstance = it }
            }
        }

        private fun createDatabase(context: Context): MusicPlayerDB {
            return Room.databaseBuilder(context, MusicPlayerDB::class.java, "MusicPlayerDB.db")
                .build()
        }
    }
}