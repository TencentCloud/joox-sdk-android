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
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.search.model.SearchHintModel
import com.tencent.joox.sdk.business.search.widget.SearchHintSectionAdapter
import com.tencent.joox.sdk.data.entity.PageState
import com.tencent.joox.sdk.databinding.SearchHintLayoutBinding
import com.tencent.joox.sdk.utils.ui.equilibriumAssignmentOfLinear

class SearchHintFragment : Fragment() {

    private lateinit var binding: SearchHintLayoutBinding
    private val model: SearchHintModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SearchHintModel::class.java)
    }

    private val hintSectionAdapter by lazy { SearchHintSectionAdapter(arrayListOf()) }
    private val helper: QuickAdapterHelper by lazy { QuickAdapterHelper.Builder(hintSectionAdapter).build() }

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
        binding = SearchHintLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val word = arguments?.getString("KEY_WORD") ?:""
        model.init(word)
        initUi()
        observerPageDataState()
    }

    private fun initUi() {
        val itemDecoration = createItemDecoration()
        binding.searchRecyclerView.addItemDecoration(itemDecoration)
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.searchRecyclerView.adapter = helper.adapter
        hintSectionAdapter.isEmptyViewEnable = true
        hintSectionAdapter.setEmptyViewLayout(requireContext(), R.layout.loading_view)
    }


    private fun onRefresh(){
        val word = arguments?.getString("KEY_WORD") ?:""
        model.doSearch(word)
    }

    private fun observerPageDataState() {
        model.observerPageDataState(this) {
            when (it.state) {
                PageState.SUCCESS -> {
                    hintSectionAdapter.submitList(it.rsp)
                }
                PageState.EMPTY -> {
                    hintSectionAdapter.submitList(emptyList())
                    hintSectionAdapter.emptyView = emptyDataView
                }
                else -> {
                    hintSectionAdapter.emptyView = errorView
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

    override fun onDestroyView() {
        super.onDestroyView()
        model.unInit()
    }

}