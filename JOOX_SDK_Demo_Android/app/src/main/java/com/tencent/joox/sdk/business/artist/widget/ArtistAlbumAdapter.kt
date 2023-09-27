package com.tencent.joox.sdk.business.artist.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tencent.joox.sdk.business.songlist.MusicPlayListActivity
import com.tencent.joox.sdk.business.songlist.entity.AlbumEntity
import com.tencent.joox.sdk.business.songlist.entity.MusicPlayListType
import com.tencent.joox.sdk.business.subscribe.entity.SubscribePlayListEntity
import com.tencent.joox.sdk.databinding.LibrarySubscribePlayListLayoutBinding


class ArtistAlbumAdapter(data: List<AlbumEntity>) :
    BaseQuickAdapter<AlbumEntity, ArtistAlbumAdapter.ArtistAlbumItemVH>(data) {

    class ArtistAlbumItemVH(val viewBinding: LibrarySubscribePlayListLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): ArtistAlbumItemVH {
        val viewBinding =
            LibrarySubscribePlayListLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ArtistAlbumItemVH(viewBinding)
    }

    override fun onBindViewHolder(
        holder: ArtistAlbumItemVH,
        position: Int,
        item: AlbumEntity?
    ) {
        if (item == null) return
        holder.viewBinding.iconTrack.visibility = View.GONE
        holder.viewBinding.itemName.text = item.name
        Glide.with(holder.viewBinding.coverImg).load(item.coverList?.first()?.url).into(holder.viewBinding.coverImg)
        holder.viewBinding.root.setOnClickListener {
            MusicPlayListActivity.toSongList(context, MusicPlayListType.EDITOR_RECOMMEND_ALBUM.type, item.id)
        }
    }

}
