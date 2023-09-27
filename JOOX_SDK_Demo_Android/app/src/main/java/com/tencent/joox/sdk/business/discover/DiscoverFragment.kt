package com.tencent.joox.sdk.business.discover

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
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.discover.widget.DiscoverSectionAdapter
import com.tencent.joox.sdk.business.search.model.SearchHintModel
import com.tencent.joox.sdk.data.entity.PageState
import com.tencent.joox.sdk.databinding.FragmentDiscoverLayoutBinding
import com.tencent.joox.sdk.utils.ui.equilibriumAssignmentOfLinear

class DiscoverFragment : Fragment() {

    private val model: DiscoverViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DiscoverViewModel::class.java)
    }
    private lateinit var binding: FragmentDiscoverLayoutBinding
    private val discoverSectionAdapter by lazy { DiscoverSectionAdapter(arrayListOf()) }
    private val helper: QuickAdapterHelper by lazy { QuickAdapterHelper.Builder(discoverSectionAdapter).build() }

    private val emptyDataView: View
        get() {
            val notDataView = layoutInflater.inflate(R.layout.empty_view, binding.discoverRecyclerView, false)
            notDataView.setOnClickListener { onRefresh() }
            return notDataView
        }

    private val errorView: View
        get() {
            val errorView = layoutInflater.inflate(R.layout.error_view, binding.discoverRecyclerView, false)
            errorView.setOnClickListener { onRefresh() }
            return errorView
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDiscoverLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        fetch()
    }

    private fun initUi() {
        val itemDecoration = createItemDecoration()
        binding.discoverRecyclerView.addItemDecoration(itemDecoration)
        binding.discoverRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.discoverRecyclerView.adapter = helper.adapter
        discoverSectionAdapter.isEmptyViewEnable = true
        discoverSectionAdapter.setEmptyViewLayout(requireContext(), R.layout.loading_view)
    }

    private fun fetch() {
        onRefresh()
        observerPageDataState()
    }

    private fun onRefresh() {
        discoverSectionAdapter.setEmptyViewLayout(requireContext(), R.layout.loading_view)
        model.load()
    }

    private fun observerPageDataState() {
        model.observerPageDataState(this) {
            when (it.state) {
                PageState.SUCCESS -> {
                    discoverSectionAdapter.submitList(it.rsp)
                }
                PageState.EMPTY -> {
                    discoverSectionAdapter.emptyView = emptyDataView
                }
                else -> {
                    discoverSectionAdapter.emptyView = errorView
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
}