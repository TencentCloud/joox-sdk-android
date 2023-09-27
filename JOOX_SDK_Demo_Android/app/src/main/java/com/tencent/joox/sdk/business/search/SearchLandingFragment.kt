package com.tencent.joox.sdk.business.search

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.QuickAdapterHelper
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.search.model.SearchLandingViewModel
import com.tencent.joox.sdk.business.search.widget.SearchLandingSectionAdapter
import com.tencent.joox.sdk.data.entity.PageState
import com.tencent.joox.sdk.databinding.FragmentSearchLandingLayoutBinding
import com.tencent.joox.sdk.utils.ui.equilibriumAssignmentOfLinear

class SearchLandingFragment : Fragment(), View.OnClickListener {
    private val model = SearchLandingViewModel()

    private lateinit var binding: FragmentSearchLandingLayoutBinding
    private val searchSectionAdapter by lazy { SearchLandingSectionAdapter(arrayListOf()) }
    private val helper: QuickAdapterHelper by lazy { QuickAdapterHelper.Builder(searchSectionAdapter).build() }

    private val emptyDataView: View
        get() {
            val notDataView = layoutInflater.inflate(R.layout.empty_view, binding.searchRecyclerView, false)
            notDataView.setOnClickListener { onRefresh() }
            return notDataView
        }

    private val errorView: View
        get() {
            val errorView = layoutInflater.inflate(R.layout.error_view, binding.searchRecyclerView, false)
            errorView.setOnClickListener { onRefresh() }
            return errorView
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSearchLandingLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        fetch()
    }

    private fun initUi() {
        val itemDecoration = createItemDecoration()
        binding.searchBar.setOnClickListener(this)
        binding.searchRecyclerView.addItemDecoration(itemDecoration)
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.searchRecyclerView.adapter = helper.adapter
        searchSectionAdapter.isEmptyViewEnable = true
        searchSectionAdapter.setEmptyViewLayout(requireContext(), R.layout.loading_view)
    }

    private fun fetch() {
        onRefresh()
        observerPageDataState()
    }

    private fun onRefresh() {
        searchSectionAdapter.setEmptyViewLayout(requireContext(), R.layout.loading_view)
        model.load()
    }

    private fun observerPageDataState() {
        model.observerPageDataState(this) {
            when (it.state) {
                PageState.SUCCESS -> {
                    searchSectionAdapter.submitList(it.rsp)
                }
                PageState.EMPTY -> {
                    searchSectionAdapter.emptyView = emptyDataView
                }
                else -> {
                    searchSectionAdapter.emptyView = errorView
                }
            }
        }
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return object : RecyclerView.ItemDecoration() {
            private val unit = 20
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                equilibriumAssignmentOfLinear(unit, outRect, view, parent)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.searchBar -> {
                SearchActivity.toSearch(requireContext())
            }
        }
    }
}