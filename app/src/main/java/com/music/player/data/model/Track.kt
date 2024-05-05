package com.music.player.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Entity(tableName = "playlist")
data class Track(
    @PrimaryKey
    val id: Int,
    val title: String,
    val fileSource: String,
    var duration: Long,
    val thumbnail: String,
    val isFavourite: Boolean = false,
    var timer: String = timeConvertor(duration)

)

fun timeConvertor(duration: Long): String {
    return Date(duration).run {
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val timeFormat = formatter.format(this)
        val result = timeFormat.split(":")
        if (result[0] == "00") "${result[1]}:${result[2]}" else timeFormat
    }
}
