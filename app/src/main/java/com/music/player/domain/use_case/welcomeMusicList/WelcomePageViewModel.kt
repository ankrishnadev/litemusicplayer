package com.music.player.domain.use_case.welcomeMusicList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.music.player.data.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel for getting Music List from mobile and  Connecting UseCase and Activity of Welcome Page
@HiltViewModel
class WelcomePageViewModel @Inject constructor(val fetchMusicListUseCase: FetchMusicListUseCase) :
    ViewModel() {

    private val _audioPlayList = MutableLiveData<ArrayList<Track>>()
    val audioPlayList: LiveData<ArrayList<Track>> get() = _audioPlayList

    fun getLocalAudios() {
        viewModelScope.launch(Dispatchers.IO) {
            _audioPlayList.postValue(fetchMusicListUseCase())
        }
    }


}