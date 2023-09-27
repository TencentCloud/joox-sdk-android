package com.tencent.joox.sdk.business.search

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.QuickAdapterHelper
import com.chad.library.adapter.base.loadState.LoadState
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.search.entity.SearchConstant
import com.tencent.joox.sdk.business.search.entity.SearchTab
import com.tencent.joox.sdk.business.search.model.SearchTabModel
import com.tencent.joox.sdk.business.search.widget.SearchTabAdapter
import com.tencent.joox.sdk.business.songlist.widget.MusicTrailingLoadStateAdapter
import com.tencent.joox.sdk.data.entity.PageState
import com.tencent.joox.sdk.databinding.SearchTabLayoutBinding
import com.tencent.joox.sdk.utils.ui.equilibriumAssignmentOfLinear

class SearchTabFragment : Fragment(), TrailingLoadStateAdapter.OnTrailingListener {

    private lateinit var binding: SearchTabLayoutBinding
    private val sectionAdapter by lazy { SearchTabAdapter(arrayListOf()) }
    private val model: SearchTabModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SearchTabModel::class.java)
    }
    private val trailingLoadStateAdapter by lazy { MusicTrailingLoadStateAdapter() }

    private val helper: QuickAdapterHelper by lazy {
        QuickAdapterHelper.Builder(sectionAdapter)
                .setTrailingLoadStateAdapter(trailingLoadStateAdapter)
                .build()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = SearchTabLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observerPageDataState()
        onRefresh()
    }

    private fun initUi() {
        sectionAdapter.isEmptyViewEnable = true
        val itemDecoration = createItemDecoration()
        binding.searchTabRv.addItemDecoration(itemDecoration)
        sectionAdapter.setEmptyViewLayout(requireContext(), R.layout.loading_view)
        binding.searchTabRv.adapter = helper.adapter
        binding.searchTabRv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        trailingLoadStateAdapter.setOnLoadMoreListener(this)
    }


    private fun onRefresh() {
        arguments?.let {
            val type = it.getSerializable(SearchConstant.KEY_SEARCH_TYPE)
            val word = it.getString(SearchConstant.KEY_SEARCH) ?: ""
            type?.let {
                model.doSearch(type as SearchTab, word)
            }
        }
    }

    private fun observerPageDataState() {
        model.observerPageDataState(this) {
                when (it.state) {
                    PageState.SUCCESS -> {
                        it.rsp?.let { itemList ->
                            if (sectionAdapter.items.isEmpty()) {
                                sectionAdapter.submitList(itemList)
                            } else {
                                sectionAdapter.addAll(itemList)
                            }
                            if (!it.pageIndex.hasMore) {
                                helper.trailingLoadState = LoadState.NotLoading(true)
                            } else {
                                helper.trailingLoadState = LoadState.NotLoading(false)
                            }
                        }
                    }
                    PageState.EMPTY -> {
                        helper.trailingLoadState = LoadState.NotLoading(true)
                    }
                    else -> {
                        helper.trailingLoadState = LoadState.NotLoading(true)
                    }
                }
            }
    }

    override fun onFailRetry() {
    }

    override fun onLoad() {
        onRefresh()
    }


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