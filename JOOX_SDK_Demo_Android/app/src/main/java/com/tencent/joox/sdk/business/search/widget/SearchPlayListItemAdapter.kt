package com.tencent.joox.sdk.business.search.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tencent.joox.sdk.business.search.entity.SearchPlayListEntity
import com.tencent.joox.sdk.business.songlist.MusicPlayListActivity
import com.tencent.joox.sdk.business.songlist.entity.MusicPlayListType
import com.tencent.joox.sdk.databinding.SearchHotWordItemLayoutBinding
import com.tencent.joox.sdk.databinding.SearchLandingPlayListLayoutBinding


class SearchPlayListItemAdapter(data: List<SearchPlayListEntity>) : BaseQuickAdapter<SearchPlayListEntity, SearchPlayListItemAdapter.ItemVH>(data) {

    class ItemVH(val viewBinding: SearchLandingPlayListLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)


    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): ItemVH {
        val viewBinding =
            SearchLandingPlayListLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemVH(viewBinding)
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int, item: SearchPlayListEntity?) {
        if (item == null) return
        holder.viewBinding.itemName.text = item.name
        holder.viewBinding.trackCount.text = item.trackCount.toString()
        Glide.with(holder.viewBinding.coverImg).load(item.coverUrl?.first()?.url).into(holder.viewBinding.coverImg)
        holder.viewBinding.root.setOnClickListener {
            MusicPlayListActivity.toSongList(context, item.id)
        }
    }
}
