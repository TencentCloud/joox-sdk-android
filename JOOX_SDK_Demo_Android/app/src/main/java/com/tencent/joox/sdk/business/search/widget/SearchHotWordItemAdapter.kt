package com.tencent.joox.sdk.business.search.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tencent.joox.sdk.business.search.entity.HotWordEntity
import com.tencent.joox.sdk.databinding.SearchHotWordItemLayoutBinding


class SearchHotWordItemAdapter(data: List<HotWordEntity>) : BaseQuickAdapter<HotWordEntity, SearchHotWordItemAdapter.ItemVH>(data) {

    class ItemVH(val viewBinding: SearchHotWordItemLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)


    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): ItemVH {
        val viewBinding =
            SearchHotWordItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemVH(viewBinding)
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int, item: HotWordEntity?) {
        if (item == null) return
        holder.viewBinding.hotWordName.text = item.word
    }
}
