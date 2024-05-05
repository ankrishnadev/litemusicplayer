package com.music.player.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.music.player.common.CommonUtil.scaleDown
import com.music.player.common.CommonUtil.scaleOriginal
import com.music.player.common.CommonUtil.setOnGlideImage
import com.music.player.data.model.Track
import com.music.player.databinding.ViewPlayListItemBinding
import java.util.Locale


class PlaylistAdapter(private var playlist: List<Track>, val onItemClick: (Track, Int) -> Unit) :
    RecyclerView.Adapter<ViewHolder>(),
    Filterable {
    private var values = playlist

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return TrackViewHolder(
            ViewPlayListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as TrackViewHolder).bind(values[position], onItemClick, position)
    }

    class TrackViewHolder(private val binding: ViewPlayListItemBinding) : ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(track: Track, onItemClick: (Track, Int) -> Unit, position: Int) {
            setOnGlideImage(binding.root.context, track.thumbnail, binding.trackThumbnail)
            binding.trackTitle.text = track.title
            binding.trackSource.text = track.fileSource.split("/")[4]
            binding.trackDuration.text = track.timer

            binding.root.setOnTouchListener { view, event ->
                setOnTouchListener(view, event, onItemClick, track)
                return@setOnTouchListener true
            }


        }

        private fun setOnTouchListener(
            view: View,
            event: MotionEvent,
            onItemClick: (Track, Int) -> Unit,
            track: Track
        ) {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> scaleDown(view)
                MotionEvent.ACTION_CANCEL -> scaleOriginal(view)
                MotionEvent.ACTION_UP -> {
                    scaleOriginal(view)
                    onItemClick.invoke(track, position)
                }
            }

        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString().lowercase(Locale.ROOT)
                if (charSearch.isEmpty()) {
                    values = playlist
                } else {
                    values = ArrayList<Track>()
                    val searchResults = playlist.filter {
                        it.title.lowercase(Locale.ROOT).contains(charSearch)
                    }
                    (values as ArrayList<Track>).addAll(searchResults)
                }
                return filterResults.also {
                    it.values = values
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                values = results?.values as ArrayList<Track>
                notifyDataSetChanged()
            }
        }
    }

    fun searchFromList(s: String) {
        filter.filter(s)
    }
}