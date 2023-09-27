package com.tencent.joox.sdk.business.search.widget.tab

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
import com.tencent.joox.sdk.databinding.MusicSongItemLayoutBinding
import com.tencent.joox.sdk.databinding.SearchSongItemLayoutBinding


class SearchSongAdapter(data: List<TrackItem>) : BaseQuickAdapter<TrackItem, SearchSongAdapter.SongItemVH>(data) {

    class SongItemVH(val viewBinding: SearchSongItemLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onBindViewHolder(holder: SongItemVH, position: Int, item: TrackItem?) {
        if (item == null) return
        holder.viewBinding.songName.text = item.name
        if (item.auth_terminal == 1){
            holder.viewBinding.songName.setTextColor(context.getColor(android.R.color.white))
        }else{
            holder.viewBinding.songName.setTextColor(context.getColor(R.color.grey_66ffff))
        }
        holder.viewBinding.songArtistName.text = generateArtistsName(item)
        holder.viewBinding.songVipLabel.visibility = if (item.vip_flag == 1) View.VISIBLE else View.GONE
        holder.viewBinding.songHifiLabel.visibility = if (item.has_hifi == 1) View.VISIBLE else View.GONE
        Glide.with(holder.viewBinding.coverImg).load(item.images?.first()?.url).into(holder.viewBinding.coverImg)
        holder.viewBinding.root.setOnClickListener {
            AppPlayManager.getInstance().playMusicList(items, position, true)
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): SongItemVH {
        val viewBinding =
            SearchSongItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return SongItemVH(viewBinding)
    }


    private fun generateArtistsName(item: TrackItem): String {
        val artistNames = arrayListOf<String>()
        item.artist_list?.forEach { artistListBean ->
            artistNames.add(artistListBean.name)
        }
        return artistNames.joinToString(",") ?: ""
    }

    companion object {
        private const val ITEM_TYPE = 10
    }


}
