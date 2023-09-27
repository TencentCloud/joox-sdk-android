package com.tencent.joox.sdk.business.discover.widget

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.tencent.joox.sdk.business.songlist.MusicPlayListActivity
import com.tencent.joox.sdk.data.entity.DiscoverSectionEntity
import com.tencent.joox.sdk.databinding.CommonSectionLayoutBinding
import com.tencent.joox.sdk.utils.ui.equilibriumAssignmentOfLinear

class DiscoverSectionAdapter(data: List<DiscoverSectionEntity>) : BaseMultiItemAdapter<DiscoverSectionEntity>(data) {

    private val itemDecoration: RecyclerView.ItemDecoration by lazy { createItemDecoration() }
    class SectionVH(val viewBinding: CommonSectionLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root)

    init {
        addItemType(SECTION_TYPE, object : OnMultiItemAdapterListener<DiscoverSectionEntity, SectionVH> {
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SectionVH {
                val viewBinding =
                    CommonSectionLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
                viewBinding.sectionItemList.addItemDecoration(itemDecoration)
                viewBinding.sectionItemList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                viewBinding.sectionItemList.adapter = DiscoverSectionItemAdapter(emptyList())
                return SectionVH(viewBinding)
            }

            override fun onBind(holder: SectionVH, position: Int, sectionEntity: DiscoverSectionEntity?) {
                sectionEntity?.let {
                    holder.viewBinding.sectionTitleTv.text = sectionEntity.title
                    val itemAdapter = holder.viewBinding.sectionItemList.adapter
                    if (itemAdapter is DiscoverSectionItemAdapter) {
                        itemAdapter.submitList(sectionEntity.itemList)
                        itemAdapter.setOnItemClickListener { adapter, view, position ->
                            val sectionItemEntity = adapter.getItem(position)
                            MusicPlayListActivity.toSongList(context, sectionEntity.type, sectionItemEntity?.id ?: "")
                        }
                    }
                }
            }

            override fun isFullSpanItem(itemType: Int): Boolean {
                return true
            }

        }).onItemViewType { position, list ->
            SECTION_TYPE
        }

    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return object : RecyclerView.ItemDecoration() {
            private val unit = 5
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                equilibriumAssignmentOfLinear(unit, outRect, view, parent)
            }
        }
    }

    companion object {
        private const val SECTION_TYPE = 11
    }
}
