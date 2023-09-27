package com.tencent.joox.sdk.business.search.widget

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.chad.library.adapter.base.layoutmanager.QuickGridLayoutManager
import com.tencent.joox.sdk.business.search.SearchActivity
import com.tencent.joox.sdk.business.search.entity.HotWordEntity
import com.tencent.joox.sdk.business.search.entity.SearchLandingSectionEntity
import com.tencent.joox.sdk.business.search.entity.SearchLandingType
import com.tencent.joox.sdk.business.search.entity.SearchPlayListEntity
import com.tencent.joox.sdk.data.entity.ArtistEntity
import com.tencent.joox.sdk.databinding.SearchLandingSectionLayoutBinding
import com.tencent.joox.sdk.utils.ui.equilibriumAssignmentOfGrid

class SearchLandingSectionAdapter(data: ArrayList<SearchLandingSectionEntity>) :
    BaseMultiItemAdapter<SearchLandingSectionEntity>(data) {

    private val itemDecoration: RecyclerView.ItemDecoration by lazy { createItemDecoration() }

    class SectionVH(val viewBinding: SearchLandingSectionLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)

    init {
        addItemType(SearchLandingType.HOTWORD.ordinal, HotWordSectionAdapter())
                .addItemType(SearchLandingType.SINGER.ordinal, SingerSectionAdapter())
                .addItemType(SearchLandingType.PLAYLIST.ordinal, PlayListSectionAdapter())
                .onItemViewType { position, list ->
                    list[position].type.ordinal
                }

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

    inner class HotWordSectionAdapter : OnMultiItemAdapterListener<SearchLandingSectionEntity, SectionVH> {
        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SectionVH {
            val viewBinding =
                SearchLandingSectionLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            viewBinding.sectionItemList.layoutManager = QuickGridLayoutManager(context, 3, RecyclerView.HORIZONTAL, false)
            viewBinding.sectionItemList.adapter = SearchHotWordItemAdapter(emptyList())
            return SectionVH(viewBinding)

        }

        override fun onBind(holder: SectionVH, position: Int, sectionEntity: SearchLandingSectionEntity?) {
            sectionEntity?.let {
                holder.viewBinding.sectionTitleTv.text = sectionEntity.title
                val itemAdapter = holder.viewBinding.sectionItemList.adapter
                if (itemAdapter is SearchHotWordItemAdapter) {
                    itemAdapter.submitList(sectionEntity.itemList as List<HotWordEntity>)
                    itemAdapter.setOnItemClickListener { adapter, _, position ->
                        SearchActivity.toSearch(context, adapter.items[position].word)
                    }
                }
            }
        }

    }

    inner class SingerSectionAdapter : OnMultiItemAdapterListener<SearchLandingSectionEntity, SectionVH> {

        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SectionVH {
            val viewBinding =
                SearchLandingSectionLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            viewBinding.sectionItemList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            viewBinding.sectionItemList.adapter = SearchSingerItemAdapter(emptyList())
            return SectionVH(viewBinding)
        }

        override fun onBind(holder: SectionVH, position: Int, sectionEntity: SearchLandingSectionEntity?) {
            sectionEntity?.let {
                holder.viewBinding.sectionTitleTv.text = sectionEntity.title
                val itemAdapter = holder.viewBinding.sectionItemList.adapter
                if (itemAdapter is SearchSingerItemAdapter) {
                    itemAdapter.submitList(sectionEntity.itemList as List<ArtistEntity>)
                    itemAdapter.setOnItemClickListener { adapter, view, position ->

                    }
                }
            }
        }

    }

    inner class PlayListSectionAdapter : OnMultiItemAdapterListener<SearchLandingSectionEntity, SectionVH> {

        override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SectionVH {
            val viewBinding =
                SearchLandingSectionLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            viewBinding.sectionItemList.addItemDecoration(itemDecoration)
            viewBinding.sectionItemList.layoutManager = QuickGridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
            viewBinding.sectionItemList.adapter = SearchPlayListItemAdapter(emptyList())
            return SectionVH(viewBinding)
        }

        override fun onBind(holder: SectionVH, position: Int, sectionEntity: SearchLandingSectionEntity?) {
            sectionEntity?.let {
                holder.viewBinding.sectionTitleTv.text = sectionEntity.title
                val itemAdapter = holder.viewBinding.sectionItemList.adapter
                if (itemAdapter is SearchPlayListItemAdapter) {
                    itemAdapter.submitList(sectionEntity.itemList as List<SearchPlayListEntity>)
                    itemAdapter.setOnItemClickListener { adapter, view, position ->

                    }
                }
            }
        }

    }
}
