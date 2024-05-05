package com.music.player.common

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.music.player.data.model.Track
import java.io.File

const val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

val collection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
} else {
    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
}

val projection = arrayOf(
    MediaStore.Audio.Media.TITLE,
    MediaStore.Audio.Media.DATA,
    MediaStore.Audio.Media.DURATION,
    MediaStore.Audio.Media.ALBUM_ID,
    MediaStore.Audio.Media.ARTIST,
)

fun ContentResolver.getAudios(): ArrayList<Track> {
    val audioResult = arrayListOf<Track>()
    val cursor = query(collection, projection, selection, null, null)
    var songId = 0
    while (cursor != null && cursor.moveToNext()) {
        songId++
        val songData =
            Track(
                id = songId,
                title = cursor.getString(0),
                fileSource = cursor.getString(1),
                duration = cursor.getString(2).toLong(),
                thumbnail = Uri.withAppendedPath(
                    Uri.parse("content://media/external/audio/albumart"),
                    cursor.getString(3)
                ).toString(),
                isFavourite = false
            )
        if (File(songData.fileSource).exists()) {
            audioResult.add(songData)
        }
    }

    return audioResult
}