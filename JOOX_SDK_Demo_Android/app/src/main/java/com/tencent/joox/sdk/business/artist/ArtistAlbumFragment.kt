package com.tencent.joox.sdk.business.artist

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
import com.chad.library.adapter.base.layoutmanager.QuickGridLayoutManager
import com.chad.library.adapter.base.loadState.LoadState
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.artist.entity.ArtistHomeConstant
import com.tencent.joox.sdk.business.artist.model.ArtistAlbumTabModel
import com.tencent.joox.sdk.business.artist.widget.ArtistAlbumAdapter
import com.tencent.joox.sdk.business.search.entity.SearchConstant
import com.tencent.joox.sdk.business.search.entity.SearchTab
import com.tencent.joox.sdk.business.search.model.SearchTabModel
import com.tencent.joox.sdk.business.search.widget.SearchTabAdapter
import com.tencent.joox.sdk.business.songlist.widget.MusicTrailingLoadStateAdapter
import com.tencent.joox.sdk.data.entity.PageState
import com.tencent.joox.sdk.databinding.ArtistTabLayoutBinding
import com.tencent.joox.sdk.databinding.SearchTabLayoutBinding
import com.tencent.joox.sdk.utils.ui.equilibriumAssignmentOfGrid
import com.tencent.joox.sdk.utils.ui.equilibriumAssignmentOfLinear

class ArtistAlbumFragment : Fragment(), TrailingLoadStateAdapter.OnTrailingListener {

    private lateinit var binding: ArtistTabLayoutBinding
    private val sectionAdapter by lazy { ArtistAlbumAdapter(arrayListOf()) }
    private val model: ArtistAlbumTabModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(ArtistAlbumTabModel::class.java)
    }
    private val trailingLoadStateAdapter by lazy { MusicTrailingLoadStateAdapter() }

    private val helper: QuickAdapterHelper by lazy {
        QuickAdapterHelper.Builder(sectionAdapter)
                .setTrailingLoadStateAdapter(trailingLoadStateAdapter)
                .build()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ArtistTabLayoutBinding.inflate(inflater, container, false)
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
        binding.songRv.addItemDecoration(itemDecoration)
        binding.songRv.layoutManager = QuickGridLayoutManager(requireContext(),3)
        binding.songRv.adapter = helper.adapter
        trailingLoadStateAdapter.setOnLoadMoreListener(this)
    }


    private fun onRefresh() {
        arguments?.let { it ->
            val id = it.getString(ArtistHomeConstant.KEY_ARTIST_ID) ?: ""
            model.load(id)
        }
    }

    private fun observerPageDataState() {
        model.observerPageDataState(this) {
                when (it.state) {
                    PageState.SUCCESS -> {
                        it.rsp?.albums?.items?.let { itemList ->
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
            private val unit = 10
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                equilibriumAssignmentOfGrid(unit, outRect, view, parent)
            }
        }
    }

}