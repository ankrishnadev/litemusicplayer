package com.music.player.presentation.musicPlayer

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.music.player.R
import com.music.player.common.CommonUtil.durationConvertorToMMss
import com.music.player.common.CommonUtil.setOnGlideImage
import com.music.player.common.PlayerInstance
import com.music.player.data.model.Track
import com.music.player.data.viewmodel.MusicPlayerViewModel
import com.music.player.databinding.MusicPlayerBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


//Activity for DisplayingMusic Player
@AndroidEntryPoint
class MusicPlayer : AppCompatActivity() {

    private lateinit var binding: MusicPlayerBinding
    var mediaPlayer: MediaPlayer = PlayerInstance.getInstance()
    private val musicPlayerViewModel: MusicPlayerViewModel by viewModels()

    private lateinit var currentTrack: Track

    companion object {
        private var playList = mutableListOf<Track>()
        private var isFavorite: Boolean = false

        fun getStartIntent(
            context: Context, playlist: ArrayList<Track>, isFavourite: Boolean = false
        ) {
            this.playList = playlist
            this.isFavorite = isFavourite
            context.startActivity(Intent(context, MusicPlayer::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setData()
        setOnFavourite()
        runOnUiThread(object : Runnable {
            override fun run() {
                binding.playerProgressBar.progress = mediaPlayer.currentPosition
                binding.trackCurrentTime.text =
                    durationConvertorToMMss(mediaPlayer.currentPosition.toLong())

                if (mediaPlayer.isPlaying) {
                    binding.pausePlay.setImageResource(R.drawable.stop)
                } else {
                    binding.pausePlay.setImageResource(R.drawable.play)
                }
                binding.root.postDelayed(this, 100)
            }
        })

        binding.playerProgressBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.favoriteImg.setOnClickListener {
            if (isFavorite.not()) {
                binding.favoriteImg.setImageResource(R.drawable.favorite_24)
                musicPlayerViewModel.onFavorite(currentTrack, applicationContext)
                isFavorite = false
            } else {
                binding.favoriteImg.setImageResource(R.drawable.favorite_border_24)
                musicPlayerViewModel.onUnFavorite(currentTrack, applicationContext)
                isFavorite = true
            }
        }

        binding.navBack.setOnClickListener { onBackPressed() }

    }

    private fun setData() {
        currentTrack = playList[PlayerInstance.currentIndex]
        binding.songTitle.text = currentTrack.title
        binding.trackTotalTime.text = durationConvertorToMMss(currentTrack.duration)

        binding.pausePlay.setOnClickListener { pausePlay() }
        binding.next.setOnClickListener { playNextSong() }
        binding.previous.setOnClickListener { playPreviousSong() }
        setOnGlideImage(binding.root.context, currentTrack.thumbnail, binding.trackThumbnail)

        playMusic()
    }

    private fun setOnFavourite() {
        if (isFavorite) {
            binding.favoriteImg.setImageResource(R.drawable.favorite_24)
        } else {
            binding.favoriteImg.setImageResource(R.drawable.favorite_border_24)
        }
    }

    private fun playMusic() {
        mediaPlayer.reset()
        try {
            mediaPlayer.setDataSource(currentTrack.fileSource)
            mediaPlayer.prepare()
            mediaPlayer.start()
            binding.playerProgressBar.progress = 0
            binding.playerProgressBar.setMax(mediaPlayer.duration)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun pausePlay() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause()
        else mediaPlayer.start()
    }

    private fun playNextSong() {
        if (PlayerInstance.currentIndex == playList.size - 1) return
        PlayerInstance.currentIndex += 1
        mediaPlayer.reset()
        setData()
    }

    private fun playPreviousSong() {
        if (PlayerInstance.currentIndex == 0) return
        PlayerInstance.currentIndex -= 1
        mediaPlayer.reset()
        setData()
    }
}