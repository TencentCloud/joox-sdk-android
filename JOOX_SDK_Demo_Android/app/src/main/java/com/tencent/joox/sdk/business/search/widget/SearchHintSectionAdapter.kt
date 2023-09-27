package com.tencent.joox.sdk.business.search.widget

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.artist.ArtistActivity
import com.tencent.joox.sdk.business.search.entity.SearchConstant
import com.tencent.joox.sdk.business.search.entity.SearchFrom
import com.tencent.joox.sdk.business.search.entity.SearchHintItem
import com.tencent.joox.sdk.business.search.entity.SearchKey
import com.tencent.joox.sdk.business.search.entity.SearchLandingType
import com.tencent.joox.sdk.business.search.entity.SearchPlayListEntity
import com.tencent.joox.sdk.business.search.widget.tab.SearchSongAdapter
import com.tencent.joox.sdk.business.songlist.MusicPlayListActivity
import com.tencent.joox.sdk.business.songlist.entity.AlbumEntity
import com.tencent.joox.sdk.business.songlist.widget.MusicPlayListAdapter
import com.tencent.joox.sdk.data.TrackItem
import com.tencent.joox.sdk.databinding.AlbumItemLayoutBinding
import com.tencent.joox.sdk.databinding.CommonListLayoutBinding
import com.tencent.joox.sdk.databinding.SearchPlayListLayoutBinding
import com.tencent.joox.sdk.databinding.SearchPromptItemLayoutBinding
import com.tencent.joox.sdk.databinding.SearchSingerLayoutBinding
import com.tencent.joox.sdk.utils.ui.equilibriumAssignmentOfGrid
import org.greenrobot.eventbus.EventBus

class SearchHintSectionAdapter(data: ArrayList<SearchHintItem>) :
    BaseMultiItemAdapter<SearchHintItem>(data) {

    init {
        addItemType(SearchLandingType.HOTWORD.ordinal, PromptAdapter())
                .addItemType(SearchLandingType.SINGER.ordinal, SingerSectionAdapter())
                .addItemType(SearchLandingType.PLAYLIST.ordinal, PlayListSectionAdapter())
                .addItemType(SearchLandingType.TRACT.ordinal, SongSectionAdapter())
                .addItemType(SearchLandingType.ALBUM.ordinal, AlbumSectionAdapter())
                .onItemViewType { position, list ->
                    when(list[position].type ){
                        1, 3 -> {
                            SearchLandingType.PLAYLIST.ordinal
                        }
                        2 -> {
                            SearchLandingType.ALBUM.ordinal
                        }
                        5 -> {
                            SearchLandingType.TRACT.ordinal
                        }
                        6 -> {
                            SearchLandingType.SINGER.ordinal
                        }
                        9 -> {
                            SearchLandingType.HOTWORD.ordinal
                        }
                        else -> {
                            SearchLandingType.HOTWORD.ordinal
                        }
                    }
                }

    }


    inner class PromptItemVH(val viewBinding: SearchPromptItemLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    inner class PromptAdapter : OnMultiItemAdapterListener<SearchHintItem, PromptItemVH> {

        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): PromptItemVH {
            val viewBinding =
                SearchPromptItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            return PromptItemVH(viewBinding)
        }

        override fun onBind(holder: PromptItemVH, position: Int, item: SearchHintItem?) {
            item?.promptInfo?.let {
                holder.viewBinding.hotWordName.text = it.name
            }
            holder.itemView.setOnClickListener {
                EventBus.getDefault().post(SearchKey(item?.promptInfo?.name ?:"", SearchFrom.HINT_PROMPT))
                val bundle = Bundle().also {bundle ->
                    bundle.putString(SearchConstant.KEY_SEARCH, item?.promptInfo?.name ?:"")
                }
                it.findNavController().navigate(R.id.searchTabs, bundle)
            }
        }
    }

    inner class SingerVH(val viewBinding: SearchSingerLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)
    inner class SingerSectionAdapter : OnMultiItemAdapterListener<SearchHintItem, SingerVH> {
        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SingerVH {
            val viewBinding =
                SearchSingerLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            return SingerVH(viewBinding)
        }

        override fun onBind(holder: SingerVH, position: Int, sectionEntity: SearchHintItem?) {
            sectionEntity?.singer?.let {artist ->
                holder.viewBinding.userName.text = artist.name
                Glide.with(context).load(artist.coverList?.first()?.url ?: "").error(R.drawable.icon_avatar)
                        .into(holder.viewBinding.userCoverImg)
                holder.itemView.setOnClickListener { ArtistActivity.toArtistHome(context, artist.id ?:"") }
            }
        }

    }

    inner class SongVH(val viewBinding: CommonListLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)
    inner class SongSectionAdapter : OnMultiItemAdapterListener<SearchHintItem, SongVH> {
        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SongVH {
            val viewBinding =
                CommonListLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            viewBinding.itemContent.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            viewBinding.itemContent.adapter = SearchSongAdapter(emptyList())
            return SongVH(viewBinding)
        }

        override fun onBind(holder: SongVH, position: Int, sectionEntity: SearchHintItem?) {
            val songList = arrayListOf<TrackItem>()
            sectionEntity?.song?.forEach {
               it.songInfo?.let { track ->
                   songList.add(track)
               }
            }
            val itemAdapter = holder.viewBinding.itemContent.adapter
            if (itemAdapter is SearchSongAdapter) {
                itemAdapter.submitList(songList)
            }
        }

    }


    inner class SearchPlayListVH(val viewBinding: SearchPlayListLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
    inner class PlayListSectionAdapter : OnMultiItemAdapterListener<SearchHintItem, SearchPlayListVH> {

        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SearchPlayListVH {
            val viewBinding =
                SearchPlayListLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            return SearchPlayListVH(viewBinding)
        }

        override fun onBind(holder: SearchPlayListVH, position: Int, sectionEntity: SearchHintItem?) {
            sectionEntity?.let {
                var playList : SearchPlayListEntity? = if (it.type == 1){
                    it.editorPlaylist
                }else{
                    it.userPlaylist
                }
                playList?.let { playListInfo ->
                    holder.viewBinding.itemName.text = playListInfo.name
                    holder.viewBinding.trackCount.text = playListInfo.trackCount.toString()
                    Glide.with(holder.viewBinding.coverImg).load(playListInfo.coverUrl?.first()?.url)
                            .into(holder.viewBinding.coverImg)
                    holder.viewBinding.root.setOnClickListener { _ ->
                        MusicPlayListActivity.toSongList(context, playListInfo.id)
                    }
                }
            }
        }
    }

    inner class SearchAlbumVH(val viewBinding: AlbumItemLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    inner class AlbumSectionAdapter : OnMultiItemAdapterListener<SearchHintItem, SearchAlbumVH> {

        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SearchAlbumVH {
            val viewBinding =
                AlbumItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            return SearchAlbumVH(viewBinding)
        }

        override fun onBind(holder: SearchAlbumVH, position: Int, sectionEntity: SearchHintItem?) {
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
            return artistNames.joinToString(",") ?: ""
        }
    }
}
