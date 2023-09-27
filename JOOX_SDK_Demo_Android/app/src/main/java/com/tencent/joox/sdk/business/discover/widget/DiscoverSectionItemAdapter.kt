package com.tencent.joox.sdk.business.discover.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.tencent.joox.sdk.data.entity.DiscoverItemEntity
import com.tencent.joox.sdk.databinding.DiscoverItemLayoutBinding


class DiscoverSectionItemAdapter(data: List<DiscoverItemEntity>) : BaseMultiItemAdapter<DiscoverItemEntity>(data) {

    class ItemVH(val viewBinding: DiscoverItemLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)

    init {
       addItemType(ITEM_TYPE, object : OnMultiItemAdapterListener<DiscoverItemEntity, ItemVH> {
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): ItemVH {
                val viewBinding =
                    DiscoverItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
                return ItemVH(viewBinding)
            }

            override fun onBind(holder: ItemVH, position: Int, item: DiscoverItemEntity?) {
                if (item == null) return
                holder.viewBinding.itemName.text = item.name
                Glide.with(holder.viewBinding.coverImg).load(item.coverUrl?.first()?.url).into(holder.viewBinding.coverImg)
            }

        }).onItemViewType { position, list ->
           ITEM_TYPE
       }

    }

    companion object {
        private const val ITEM_TYPE = 10
    }
}
