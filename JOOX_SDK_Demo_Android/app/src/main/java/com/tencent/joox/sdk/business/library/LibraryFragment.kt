package com.tencent.joox.sdk.business.library

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.QuickAdapterHelper
import com.chad.library.adapter.base.layoutmanager.QuickGridLayoutManager
import com.joox.sdklibrary.SDKInstance
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.library.widget.LibraryFavAdapter
import com.tencent.joox.sdk.business.library.widget.LibraryHeaderAdapter
import com.tencent.joox.sdk.business.library.widget.LibraryPlayListItemAdapter
import com.tencent.joox.sdk.business.subscribe.SubscribeManager
import com.tencent.joox.sdk.data.entity.PageState
import com.tencent.joox.sdk.databinding.FragmentLibraryLayoutBinding
import com.tencent.joox.sdk.utils.ui.getItemCount
import com.tencent.joox.sdk.utils.ui.getSpanCount

class LibraryFragment : Fragment() {

    private val model: LibraryViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(LibraryViewModel::class.java)
    }
    private lateinit var binding: FragmentLibraryLayoutBinding
    private val subPlayListAdapter by lazy { LibraryPlayListItemAdapter(arrayListOf()) }
    private val userHeaderAdapter by lazy { LibraryHeaderAdapter(SDKInstance.getIns().userManager?.userInfo) }
    private val userFavAdapter by lazy { LibraryFavAdapter(SubscribeManager.likeSongList.size) }

    private val helper: QuickAdapterHelper by lazy {
        QuickAdapterHelper.Builder(subPlayListAdapter)
                .build()
                .addBeforeAdapter(userHeaderAdapter)
                .addBeforeAdapter(userFavAdapter)
    }


    private val emptyDataView: View
        get() {
            val notDataView = layoutInflater.inflate(R.layout.footer_empty_view, binding.discoverRecyclerView, false)
            notDataView.setOnClickListener { fetch() }
            return notDataView
        }

    private val errorView: View
        get() {
            val errorView = layoutInflater.inflate(R.layout.footer_error_view, binding.discoverRecyclerView, false)
            errorView.setOnClickListener { fetch() }
            return errorView
        }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLibraryLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        val itemDecoration = createItemDecoration()
        binding.discoverRecyclerView.addItemDecoration(itemDecoration)
        binding.discoverRecyclerView.layoutManager = QuickGridLayoutManager(requireContext(),3,  RecyclerView.VERTICAL, false)
        binding.discoverRecyclerView.adapter = helper.adapter
        subPlayListAdapter.isEmptyViewEnable = true
        subPlayListAdapter.setEmptyViewLayout(requireContext(), R.layout.footer_loading_view)
        fetch()
        observerPageDataState()
    }

    private fun fetch() {
       model.load()
    }

    private fun observerPageDataState() {
        model.observerSubscribePlayListDataState(this) {
            when (it.state) {
                PageState.SUCCESS -> {
                    subPlayListAdapter.submitList(it.rsp)
                }
                PageState.EMPTY -> {
                    subPlayListAdapter.emptyView = emptyDataView
                }
                else -> {
                    subPlayListAdapter.emptyView = errorView
                }
            }
        }
    }


    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return object : RecyclerView.ItemDecoration() {
            private val unit = 5
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.left = unit
                outRect.right = unit
                outRect.top = unit * 2
                // 判断是否为最后一行，最后一行单独添加底部的间距
                val itemCount = parent.getItemCount()
                // 网格布局的跨度数
                val spanCount = parent.getSpanCount()
                // 当前 item 的 position
                val itemPosition = parent.getChildAdapterPosition(view)
                if (itemPosition in (itemCount - spanCount) until itemCount) {
                    outRect.bottom = unit * 2
                }
            }
        }
    }
}