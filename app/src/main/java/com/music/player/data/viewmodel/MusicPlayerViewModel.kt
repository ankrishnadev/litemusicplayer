package com.music.player.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.player.data.model.Track
import com.music.player.domain.db.MusicPlayerDB
import com.music.player.domain.use_case.welcomeMusicList.FetchMusicListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(val fetchMusicListUseCase: FetchMusicListUseCase) :
    ViewModel() {

    fun onFavorite(track: Track, applicationContext: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            MusicPlayerDB.getInstance(applicationContext).trackDao().insert(track)
        }
    }

    fun onUnFavorite(track: Track, applicationContext: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            MusicPlayerDB.getInstance(applicationContext).trackDao().delete(track.id)
        }
    }
}