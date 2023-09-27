package com.tencent.joox.sdk.business.search.widget

import android.content.Context
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.player.AppPlayManager
import com.tencent.joox.sdk.business.search.entity.SearchPlayListEntity
import com.tencent.joox.sdk.business.search.entity.SearchTab
import com.tencent.joox.sdk.business.search.entity.SearchTabItem
import com.tencent.joox.sdk.business.songlist.MusicPlayListActivity
import com.tencent.joox.sdk.business.songlist.entity.AlbumEntity
import com.tencent.joox.sdk.data.TrackItem
import com.tencent.joox.sdk.data.entity.ArtistEntity
import com.tencent.joox.sdk.databinding.AlbumItemLayoutBinding
import com.tencent.joox.sdk.databinding.MusicSongItemLayoutBinding
import com.tencent.joox.sdk.databinding.SearchPlayListLayoutBinding
import com.tencent.joox.sdk.databinding.SearchSingerLayoutBinding
import com.tencent.joox.sdk.databinding.SearchSongItemLayoutBinding

class SearchTabAdapter(data: ArrayList<SearchTabItem>) :
    BaseMultiItemAdapter<SearchTabItem>(data) {

    init {
        addItemType(SearchTab.ARTIST.type, SingerSectionAdapter())
                .addItemType(SearchTab.PLAYLIST.type, PlayListSectionAdapter())
                .addItemType(SearchTab.SONG.type, SongSectionAdapter())
                .addItemType(SearchTab.ALBUM.type, AlbumSectionAdapter())
                .onItemViewType { position, list ->
                    list[position].type.type
                }

    }


    inner class SingerVH(val viewBinding: SearchSingerLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)
    inner class SingerSectionAdapter : OnMultiItemAdapterListener<SearchTabItem, SingerVH> {
        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SingerVH {
            val viewBinding =
                SearchSingerLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            return SingerVH(viewBinding)
        }

        override fun onBind(holder: SingerVH, position: Int, sectionEntity: SearchTabItem?) {
            sectionEntity?.item.let {
                if (it  is ArtistEntity){
                    holder.viewBinding.userName.text = it.name
                    Glide.with(context).load(it.coverList?.first()?.url ?: "").error(R.drawable.icon_avatar)
                            .into(holder.viewBinding.userCoverImg)
                }
            }
        }

    }

    inner class SongVH(val viewBinding: SearchSongItemLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)
    inner class SongSectionAdapter : OnMultiItemAdapterListener<SearchTabItem, SongVH> {
        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SongVH {
            val viewBinding =
                SearchSongItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            return SongVH(viewBinding)
        }

        override fun onBind(holder: SongVH, position: Int, sectionEntity: SearchTabItem?) {
            sectionEntity?.item.let {songList ->
                if (songList is ArrayList<*>){
                    val song = songList.first()
                    if (song is TrackItem){
                        holder.viewBinding.songName.text = song.name
                        if (song.auth_terminal == 1){
                            holder.viewBinding.songName.setTextColor(context.getColor(android.R.color.white))
                        }else{
                            holder.viewBinding.songName.setTextColor(context.getColor(R.color.grey_66ffff))
                        }
                        holder.viewBinding.songArtistName.text = generateArtistsName(song)
                        holder.viewBinding.songVipLabel.visibility = if (song.vip_flag == 1) View.VISIBLE else View.GONE
                        holder.viewBinding.songHifiLabel.visibility = if (song.has_hifi == 1) View.VISIBLE else View.GONE
                        Glide.with(holder.viewBinding.coverImg).load(song.images?.first()?.url).into(holder.viewBinding.coverImg)
                        holder.viewBinding.root.setOnClickListener {
                            AppPlayManager.getInstance().playMusicList(arrayListOf(song), 0, true)
                        }
                    }
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

    }


    inner class SearchPlayListVH(val viewBinding: SearchPlayListLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
    inner class PlayListSectionAdapter : OnMultiItemAdapterListener<SearchTabItem, SearchPlayListVH> {

        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SearchPlayListVH {
            val viewBinding =
                SearchPlayListLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            return SearchPlayListVH(viewBinding)
        }

        override fun onBind(holder: SearchPlayListVH, position: Int, sectionEntity: SearchTabItem?) {
            sectionEntity?.item.let {
                if (it is SearchPlayListEntity){
                    holder.viewBinding.itemName.text = it.name
                    holder.viewBinding.trackCount.text = it.trackCount.toString()
                    Glide.with(holder.viewBinding.coverImg).load(it.coverUrl?.first()?.url)
                            .into(holder.viewBinding.coverImg)
                    holder.viewBinding.root.setOnClickListener { _ ->
                        MusicPlayListActivity.toSongList(context, it.id)
                    }
                }
            }
        }
    }

    inner class SearchAlbumVH(val viewBinding: AlbumItemLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    inner class AlbumSectionAdapter : OnMultiItemAdapterListener<SearchTabItem, SearchAlbumVH> {

        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SearchAlbumVH {
            val viewBinding =
                AlbumItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            return SearchAlbumVH(viewBinding)
        }

        override fun onBind(holder: SearchAlbumVH, position: Int, sectionEntity: SearchTabItem?) {
            sectionEntity?.item?.let {
                if (it is AlbumEntity){
                    holder.viewBinding.albumName.text = it.name
                    holder.viewBinding.albumArtistName.text = generateArtistsName(it)
                    holder.viewBinding.albumVipLabel.visibility = if (it.vipFlag == 1) View.VISIBLE else View.GONE
                    Glide.with(holder.viewBinding.coverImg).load(it.coverList?.first()?.url)
                            .into(holder.viewBinding.coverImg)
                    holder.viewBinding.root.setOnClickListener { _ ->
                        MusicPlayListActivity.toSongList(context, 4, it.id)
                    }

                }
            }
        }

        private fun generateArtistsName(item: AlbumEntity): String {
            val artistNames = arrayListOf<String>()
            item.artistList?.forEach { artistListBean ->
                artistNames.add(artistListBean.name)
            }
            val nameList = artistNames.joinToString(",") ?: ""
            return String(Base64.decode(nameList, Base64.DEFAULT))
        }
    }
}
