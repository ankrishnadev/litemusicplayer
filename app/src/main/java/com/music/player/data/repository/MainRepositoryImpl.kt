package com.music.player.data.repository

import android.content.ContentResolver
import com.music.player.common.getAudios
import com.music.player.data.model.Track
import com.music.player.domain.repository.MainRepository
import javax.inject.Inject

/* Class For all logical operation please add all operation here*/
class MainRepositoryImpl
@Inject constructor(private val contentResolver: ContentResolver) : MainRepository {
    override suspend fun getSongsList(): ArrayList<Track> = contentResolver.getAudios()
}