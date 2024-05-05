package com.music.player.domain.use_case.welcomeMusicList

import com.music.player.data.model.Track
import com.music.player.domain.repository.MainRepository
import javax.inject.Inject

// Class for fetching music list from mobile
class FetchMusicListUseCase @Inject constructor(private val repository: MainRepository) {
    suspend operator fun invoke(): ArrayList<Track> = repository.getSongsList()

}