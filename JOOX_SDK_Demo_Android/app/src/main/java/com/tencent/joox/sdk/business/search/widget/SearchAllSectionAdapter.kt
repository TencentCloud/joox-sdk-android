package com.tencent.joox.sdk.business.search.widget

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tencent.joox.sdk.business.search.entity.SearchSectionListEntity
import com.tencent.joox.sdk.databinding.CommonSectionLayoutBinding
import com.tencent.joox.sdk.utils.ui.equilibriumAssignmentOfLinear

class SearchAllSectionAdapter(data: ArrayList<SearchSectionListEntity>) :
    BaseQuickAdapter<SearchSectionListEntity, SearchAllSectionAdapter.BestMatchSectionVH>(data) {

    override fun onBindViewHolder(holder: BestMatchSectionVH, position: Int, sectionEntity: SearchSectionListEntity?) {
        holder.viewBinding.sectionTitleTv.text = sectionEntity?.sectionTitle
        sectionEntity?.itemList?.let {
            val itemAdapter = holder.viewBinding.sectionItemList.adapter
            if (itemAdapter is SearchBestMatchSectionAdapter) {
                itemAdapter.submitList(it)
            }
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): BestMatchSectionVH {
        val viewBinding =
            CommonSectionLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        val itemDecoration = createItemDecoration()
        viewBinding.sectionItemList.addItemDecoration(itemDecoration)
        viewBinding.sectionItemList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        viewBinding.sectionItemList.adapter = SearchBestMatchSectionAdapter(arrayListOf())
        return BestMatchSectionVH(viewBinding)
    }

    inner class BestMatchSectionVH(val viewBinding: CommonSectionLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root)


    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return object : RecyclerView.ItemDecoration() {
            private val unit = 15
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                equilibriumAssignmentOfLinear(unit, outRect, view, parent)
            }
        }
    }
}
