package com.music.player.domain.repository

import com.music.player.data.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/* Class For all logical operation please add all operation here and use in in MainRepositoryImpl*/
interface MainRepository {
    suspend fun getSongsList(): ArrayList<Track> {
        return arrayListOf()
    }

    suspend fun getSelectedSongsFromDB(): Flow<List<Track>> {
        return flow { emptyList<Track>() }
    }
}