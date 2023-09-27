package com.tencent.joox.sdk.business.songlist

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.QuickAdapterHelper
import com.chad.library.adapter.base.loadState.LoadState
import com.chad.library.adapter.base.loadState.trailing.TrailingLoadStateAdapter
import com.gyf.immersionbar.ImmersionBar
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.songlist.entity.MusicPlayListEntity
import com.tencent.joox.sdk.business.songlist.widget.MusicPlayListAdapter
import com.tencent.joox.sdk.business.songlist.widget.MusicPlayListHeaderAdapter
import com.tencent.joox.sdk.business.songlist.widget.MusicTrailingLoadStateAdapter
import com.tencent.joox.sdk.data.entity.PageState
import com.tencent.joox.sdk.databinding.ActivityMusicPlayListLayoutBinding
import com.tencent.joox.sdk.utils.ui.equilibriumAssignmentOfLinear

class MusicPlayListActivity : AppCompatActivity(), View.OnClickListener, TrailingLoadStateAdapter.OnTrailingListener {

    companion object {
        private const val KEY_ID = "id"
        private const val KEY_TYPE = "type"

        fun toSongList(ctx: Context, id: String) {
            toSongList(ctx, 0, id)
        }

        fun toSongList(ctx: Context, type: Int, id: String) {
            val it = Intent(ctx, MusicPlayListActivity::class.java)
            it.putExtra(KEY_ID, id)
            it.putExtra(KEY_TYPE, type)
            ctx.startActivity(it)
        }
    }

    private lateinit var binding: ActivityMusicPlayListLayoutBinding
    private val trailingLoadStateAdapter by lazy { MusicTrailingLoadStateAdapter() }
    private val musicPlayListAdapter by lazy { MusicPlayListAdapter(arrayListOf()) }
    private val musicPlayListHeaderAdapter by lazy { MusicPlayListHeaderAdapter() }

    private val helper: QuickAdapterHelper by lazy {
        QuickAdapterHelper.Builder(musicPlayListAdapter)
                .setTrailingLoadStateAdapter(trailingLoadStateAdapter)
                .build()
                .addBeforeAdapter(musicPlayListHeaderAdapter)
    }

    private val model: MusicPlayListViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(MusicPlayListViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayListLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        fullScreen()
        binding.toolbarHome.ivToolbarBack.visibility = View.VISIBLE
        binding.toolbarHome.ivToolbarBack.setOnClickListener(this)
        binding.toolbarHome.ivToolbarMenu.visibility = View.GONE
        binding.musicPlayListRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.musicPlayListRecyclerView.adapter = helper.adapter
        trailingLoadStateAdapter.setOnLoadMoreListener(this)
        observerPageDataState()
        onRefresh()
    }

    private fun onRefresh() {
        helper.trailingLoadState = LoadState.Loading
        val type = intent.getIntExtra(KEY_TYPE, 0)
        val id = intent.getStringExtra(KEY_ID) ?: ""
        model.load(type, id)
    }

    private fun observerPageDataState() {
        model.observerPageDataState(
            this
        ) {
            when (it.state) {
                PageState.SUCCESS -> {
                    if (musicPlayListHeaderAdapter.items.isEmpty()) {
                        musicPlayListHeaderAdapter.setItem(it.rsp, 0)
                    }
                    it.rsp?.tracks?.items?.let { trackItems ->
                        if (musicPlayListAdapter.items.isEmpty()) {
                            musicPlayListAdapter.submitList(trackItems)
                        } else {
                            musicPlayListAdapter.addAll(trackItems)
                        }
                        val oldSize = musicPlayListAdapter.items.size
                        if (oldSize >= it.pageIndex.totalSize) {
                            helper.trailingLoadState = LoadState.NotLoading(true)
                        } else {
                            helper.trailingLoadState = LoadState.NotLoading(false)
                        }
                    }
                }
                PageState.EMPTY -> {
                    if (musicPlayListHeaderAdapter.items.isEmpty()) {
                        musicPlayListHeaderAdapter.setItem(it.rsp, 0)
                    }
                    helper.trailingLoadState = LoadState.NotLoading(true)
                }
                else -> {
                    helper.trailingLoadState = LoadState.NotLoading(true)
                }
            }
        }
    }

    private fun fullScreen() {
        ImmersionBar.with(this)
                .fullScreen(true)
                .navigationBarColor(android.R.color.transparent)
                .navigationBarDarkIcon(true)
                .init()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_toolbar_back -> {
                finish()
            }
        }

    }

    override fun onFailRetry() {
    }

    override fun onLoad() {
        onRefresh()
    }


}