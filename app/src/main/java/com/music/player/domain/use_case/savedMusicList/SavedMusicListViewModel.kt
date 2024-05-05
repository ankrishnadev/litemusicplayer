package com.music.player.domain.use_case.savedMusicList

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.music.player.data.model.Track
import com.music.player.data.repository.FavouriteSongRepository
import com.music.player.domain.db.MusicPlayerDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel for getting Music List from mobile and  Connecting UseCase and Activity of Welcome Page
@HiltViewModel
class SavedMusicListViewModel @Inject constructor(val savedMusicListUseCase: SavedMusicListUseCase) :
    ViewModel() {

    //Selected songs list
    private val _favouriteAudioPlayList = MutableLiveData<List<Track>>()
    val favouriteAudioPlayList: LiveData<List<Track>> get() = _favouriteAudioPlayList

    fun getSavedList() {
        viewModelScope.launch(Dispatchers.IO) {
            _favouriteAudioPlayList.postValue(
                savedMusicListUseCase().asLiveData().value ?: emptyList()
            )
        }
    }


    fun getSelectedSongsFromDB(applicationContext: Context): LiveData<List<Track>> {
        return FavouriteSongRepository.getInstance(MusicPlayerDB.getInstance(applicationContext))
            .getSelectedSongsFromDB()
    }
}