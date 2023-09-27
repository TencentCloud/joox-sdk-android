package com.tencent.joox.sdk.business.search.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tencent.joox.sdk.business.artist.ArtistActivity
import com.tencent.joox.sdk.data.entity.ArtistEntity
import com.tencent.joox.sdk.databinding.SearchLandingSingerLayoutBinding


class SearchSingerItemAdapter(data: List<ArtistEntity>) : BaseQuickAdapter<ArtistEntity, SearchSingerItemAdapter.ItemVH>(data) {

    class ItemVH(val viewBinding: SearchLandingSingerLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)


    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): ItemVH {
        val viewBinding =
            SearchLandingSingerLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemVH(viewBinding)
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int, item: ArtistEntity?) {
        if (item == null) return
        holder.viewBinding.userName.text = item.name
        Glide.with(context).load(item.coverList?.first()?.url ?:"").into(holder.viewBinding.userCoverImg)
        holder.itemView.setOnClickListener { ArtistActivity.toArtistHome(context, item.id ?:"") }

    }
}
