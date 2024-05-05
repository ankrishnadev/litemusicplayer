package com.music.player.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.music.player.data.model.Track
import com.music.player.domain.db.MusicPlayerDB
import com.music.player.domain.db.TrackDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/* Class For all logical operation please add all operation here*/
class FavouriteSongRepository
@Inject constructor(private val trackDao: TrackDao) {

    fun getSelectedSongsFromDB(): LiveData<List<Track>> {
        return trackDao.fetchSavedTracks().asLiveData()
    }

    companion object {
        @Volatile
        private var instance: FavouriteSongRepository? = null

        fun getInstance(database: MusicPlayerDB) =
            instance ?: synchronized(this) {
                instance ?: FavouriteSongRepository(database.trackDao()).also { instance = it }
            }
    }

}