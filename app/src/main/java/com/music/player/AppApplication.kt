package com.music.player

import android.app.Application
import com.music.player.domain.db.MusicPlayerDB
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MusicPlayerDB.getInstance(this)
    }
}