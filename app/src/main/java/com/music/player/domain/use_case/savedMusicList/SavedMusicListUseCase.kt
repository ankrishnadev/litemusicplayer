package com.music.player.domain.use_case.savedMusicList

import com.music.player.data.model.Track
import com.music.player.data.repository.FavouriteSongRepository
import com.music.player.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Class for fetching music list from mobile
class SavedMusicListUseCase @Inject constructor(private val repository: MainRepository) {
    suspend operator fun invoke(): Flow<List<Track>> = repository.getSelectedSongsFromDB()


}