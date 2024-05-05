package com.music.player.presentation.savedPlayList

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.music.player.R
import com.music.player.common.PlayerInstance
import com.music.player.databinding.SavedPlaylistBinding
import com.music.player.domain.use_case.savedMusicList.SavedMusicListViewModel
import com.music.player.presentation.adapter.PlaylistAdapter
import com.music.player.presentation.musicPlayer.MusicPlayer
import dagger.hilt.android.AndroidEntryPoint

//Activity for showing saved playlist

@AndroidEntryPoint
class SavedPlayList : AppCompatActivity() {

    private lateinit var binding: SavedPlaylistBinding
    private val savedMusicListViewModel: SavedMusicListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SavedPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.commonView.homeIconImg.apply {
            setImageResource(R.drawable.arrow_back)
            setOnClickListener { onBackPressed() }
        }

        binding.commonView.pageTitle.text = resources.getString(R.string.favourites)
        binding.emptyState.emptyStateTxt.text = resources.getString(R.string.no_items)

        savedMusicListViewModel.getSelectedSongsFromDB(applicationContext)
            .observe(this) { playList ->
                if (playList.isNullOrEmpty().not()) {
                    binding.emptyState.root.isVisible = false
                    val playListAdapter = PlaylistAdapter(playList) { track, position ->
                        //Navigate to music activity
                        PlayerInstance.getInstance().reset()
                        PlayerInstance.currentIndex = position
                        MusicPlayer.getStartIntent(
                            this@SavedPlayList, playList as ArrayList, isFavourite = true
                        )
                    }
                    binding.commonView.trackPlayList.adapter = playListAdapter
                }
            }

    }
}