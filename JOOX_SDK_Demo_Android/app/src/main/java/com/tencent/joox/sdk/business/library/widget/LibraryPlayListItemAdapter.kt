package com.tencent.joox.sdk.business.library.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tencent.joox.sdk.business.songlist.MusicPlayListActivity
import com.tencent.joox.sdk.business.songlist.entity.MusicPlayListType
import com.tencent.joox.sdk.business.subscribe.entity.SubscribePlayListEntity
import com.tencent.joox.sdk.databinding.LibrarySubscribePlayListLayoutBinding


class LibraryPlayListItemAdapter(data: List<SubscribePlayListEntity.SubscribePlayList>) :
    BaseQuickAdapter<SubscribePlayListEntity.SubscribePlayList, LibraryPlayListItemAdapter.SubscribeItemVH>(data) {

    class SubscribeItemVH(val viewBinding: LibrarySubscribePlayListLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): SubscribeItemVH {
        val viewBinding =
            LibrarySubscribePlayListLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return SubscribeItemVH(viewBinding)
    }

    override fun onBindViewHolder(
        holder: SubscribeItemVH,
        position: Int,
        item: SubscribePlayListEntity.SubscribePlayList?
    ) {
        if (item == null) return
        holder.viewBinding.itemName.text = item.name
        holder.viewBinding.trackCount.text = item.trackCount.toString()
        Glide.with(holder.viewBinding.coverImg).load(item.cover?.first()?.url).into(holder.viewBinding.coverImg)
        holder.viewBinding.root.setOnClickListener {
            MusicPlayListActivity.toSongList(context, MusicPlayListType.USER_SUBSCRIBE_PLAYLIST.type, item.id)
        }
    }

}
