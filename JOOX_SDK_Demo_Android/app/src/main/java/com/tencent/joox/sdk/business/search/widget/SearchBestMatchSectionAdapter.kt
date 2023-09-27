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
import com.tencent.joox.sdk.business.artist.ArtistActivity
import com.tencent.joox.sdk.business.player.AppPlayManager
import com.tencent.joox.sdk.business.search.entity.SearchItemListEntity
import com.tencent.joox.sdk.business.search.entity.SearchLandingType
import com.tencent.joox.sdk.business.search.entity.SearchTab
import com.tencent.joox.sdk.business.songlist.MusicPlayListActivity
import com.tencent.joox.sdk.business.songlist.entity.AlbumEntity
import com.tencent.joox.sdk.data.TrackItem
import com.tencent.joox.sdk.databinding.AlbumItemLayoutBinding
import com.tencent.joox.sdk.databinding.MusicSongItemLayoutBinding
import com.tencent.joox.sdk.databinding.SearchPlayListLayoutBinding
import com.tencent.joox.sdk.databinding.SearchSingerLayoutBinding
import com.tencent.joox.sdk.databinding.SearchSongItemLayoutBinding

class SearchBestMatchSectionAdapter(data: ArrayList<SearchItemListEntity>) :
    BaseMultiItemAdapter<SearchItemListEntity>(data) {

    init {
        addItemType(SearchTab.ARTIST.type, ArtistItemAdapter())
                .addItemType(SearchTab.PLAYLIST.type, PlayListItemAdapter())
                .addItemType(SearchTab.SONG.type, SongItemAdapter())
                .addItemType(SearchTab.ALBUM.type, AlbumItemAdapter())
                .onItemViewType { position, list ->
                    when (list[position].type) {
                        1, 3 -> {
                            SearchTab.PLAYLIST.type
                        }
                        2 -> {
                            SearchTab.ALBUM.type
                        }
                        5 -> {
                            SearchTab.SONG.type
                        }
                        6 -> {
                            SearchTab.ARTIST.type
                        }
                        else ->{
                            SearchTab.SONG.type
                        }
                    }

                }
    }

    inner class ArtistVH(val viewBinding: SearchSingerLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)
    inner class ArtistItemAdapter : OnMultiItemAdapterListener<SearchItemListEntity, ArtistVH> {
        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): ArtistVH {
            val viewBinding =
                SearchSingerLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            return ArtistVH(viewBinding)
        }

        override fun onBind(holder: ArtistVH, position: Int, sectionEntity: SearchItemListEntity?) {
            sectionEntity?.artist?.let {artist ->
                holder.viewBinding.userName.text = artist.name
                Glide.with(context).load(artist.coverList?.first()?.url ?: "").error(R.drawable.icon_avatar)
                        .into(holder.viewBinding.userCoverImg)
                holder.itemView.setOnClickListener { ArtistActivity.toArtistHome(context, artist.id ?:"") }
            }
        }

    }

    inner class SongVH(val viewBinding: SearchSongItemLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)
    inner class SongItemAdapter : OnMultiItemAdapterListener<SearchItemListEntity, SongVH> {
        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SongVH {
            val viewBinding =
                SearchSongItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            return SongVH(viewBinding)
        }

        override fun onBind(holder: SongVH, position: Int, sectionEntity: SearchItemListEntity?) {
            sectionEntity?.song?.first()?.song?.let { song ->
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
    }


    inner class SearchPlayListVH(val viewBinding: SearchPlayListLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    inner class PlayListItemAdapter : OnMultiItemAdapterListener<SearchItemListEntity, SearchPlayListVH> {

        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SearchPlayListVH {
            val viewBinding =
                SearchPlayListLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            return SearchPlayListVH(viewBinding)
        }

        override fun onBind(holder: SearchPlayListVH, position: Int, sectionEntity: SearchItemListEntity?) {
            sectionEntity?.playList?.let {
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

    inner class SearchAlbumVH(val viewBinding: AlbumItemLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    inner class AlbumItemAdapter : OnMultiItemAdapterListener<SearchItemListEntity, SearchAlbumVH> {

        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SearchAlbumVH {
            val viewBinding =
                AlbumItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            return SearchAlbumVH(viewBinding)
        }

        override fun onBind(holder: SearchAlbumVH, position: Int, sectionEntity: SearchItemListEntity?) {
            sectionEntity?.album?.let {
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
