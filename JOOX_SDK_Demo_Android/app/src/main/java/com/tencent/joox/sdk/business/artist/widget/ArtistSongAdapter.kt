package com.tencent.joox.sdk.business.artist.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.player.AppPlayManager
import com.tencent.joox.sdk.data.TrackItem
import com.tencent.joox.sdk.databinding.SearchSongItemLayoutBinding

class ArtistSongAdapter(data: ArrayList<TrackItem>) :
    BaseQuickAdapter<TrackItem, ArtistSongAdapter.ArtistSongVH>(data) {
    inner class ArtistSongVH(val viewBinding: SearchSongItemLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onBindViewHolder(holder: ArtistSongVH, position: Int, trackItem: TrackItem?) {
        trackItem?.let { song ->
            holder.viewBinding.songName.text = song.name
            if (song.auth_terminal == 1) {
                holder.viewBinding.songName.setTextColor(context.getColor(android.R.color.white))
            } else {
                holder.viewBinding.songName.setTextColor(context.getColor(R.color.grey_66ffff))
            }
            holder.viewBinding.songArtistName.text = generateArtistsName(song)
            holder.viewBinding.songVipLabel.visibility = if (song.vip_flag == 1) View.VISIBLE else View.GONE
            holder.viewBinding.songHifiLabel.visibility = if (song.has_hifi == 1) View.VISIBLE else View.GONE
            Glide.with(holder.viewBinding.coverImg).load(song.images?.first()?.url)
                    .into(holder.viewBinding.coverImg)
            holder.viewBinding.root.setOnClickListener {
                AppPlayManager.getInstance().playMusicList(arrayListOf(song), 0, true)
            }
        }
    }

    private fun generateArtistsName(item: TrackItem): String {
        val artistNames = arrayListOf<String>()
        item.artist_list?.forEach { artistListBean ->
            artistNames.add(artistListBean.name)
        }
        return artistNames.joinToString(",")
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): ArtistSongVH {
        val viewBinding =
            SearchSongItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ArtistSongVH(viewBinding)
    }


}
