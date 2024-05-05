package com.music.player.common

import android.media.MediaPlayer

object PlayerInstance {
    private var instance: MediaPlayer? = null

    fun getInstance(): MediaPlayer {
        if (instance == null) {
            instance = MediaPlayer()
        }
        return instance as MediaPlayer
    }

    var currentIndex = -1
}
