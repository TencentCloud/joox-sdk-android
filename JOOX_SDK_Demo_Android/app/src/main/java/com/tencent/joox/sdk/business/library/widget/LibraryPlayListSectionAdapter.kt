package com.tencent.joox.sdk.business.library.widget

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseSingleItemAdapter
import com.chad.library.adapter.base.layoutmanager.QuickGridLayoutManager
import com.tencent.joox.sdk.business.search.entity.SearchPlayListEntity
import com.tencent.joox.sdk.business.search.widget.SearchPlayListItemAdapter
import com.tencent.joox.sdk.business.songlist.MusicPlayListActivity
import com.tencent.joox.sdk.business.songlist.MusicPlayListViewModel
import com.tencent.joox.sdk.business.songlist.entity.MusicPlayListType
import com.tencent.joox.sdk.business.subscribe.entity.SubscribePlayListEntity
import com.tencent.joox.sdk.data.entity.DiscoverItemEntity
import com.tencent.joox.sdk.databinding.DiscoverItemLayoutBinding
import com.tencent.joox.sdk.databinding.LibraryPlaylistSectionLayoutBinding
import com.tencent.joox.sdk.databinding.LibrarySubscribePlayListLayoutBinding
import com.tencent.joox.sdk.utils.ui.equilibriumAssignmentOfGrid


class LibraryPlayListSectionAdapter(data: List<SubscribePlayListEntity.SubscribePlayList>) :
    BaseSingleItemAdapter<List<SubscribePlayListEntity.SubscribePlayList>, LibraryPlayListSectionAdapter.SubscribeItemVH>(data) {

    private val itemDecoration: RecyclerView.ItemDecoration by lazy { createItemDecoration() }

    class SubscribeItemVH(val viewBinding: LibraryPlaylistSectionLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onBindViewHolder(holder: SubscribeItemVH, sectionEntity: List<SubscribePlayListEntity.SubscribePlayList>?) {
        sectionEntity?.let {
            val itemAdapter = holder.viewBinding.sectionItemList.adapter
            if (itemAdapter is LibraryPlayListItemAdapter) {
                itemAdapter.submitList(sectionEntity)
                itemAdapter.setOnItemClickListener { adapter, view, position ->

                }
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): SubscribeItemVH {
        val viewBinding =
            LibraryPlaylistSectionLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        viewBinding.sectionItemList.addItemDecoration(itemDecoration)
        viewBinding.sectionItemList.layoutManager = QuickGridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
        viewBinding.sectionItemList.adapter = LibraryPlayListItemAdapter(emptyList())
        return SubscribeItemVH(viewBinding)
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return object : RecyclerView.ItemDecoration() {
            private val unit = 5
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                equilibriumAssignmentOfGrid(unit, outRect, view, parent)
            }
        }
    }

}
