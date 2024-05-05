package com.music.player.domain.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.music.player.data.model.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(track: Track)

    @Query("SELECT * from playlist")
    fun fetchSavedTracks(): Flow<List<Track>>

    @Query("DELETE from playlist WHERE id=:id")
    fun delete(id: Int)
}