package com.tencent.joox.sdk.business.artist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.gyf.immersionbar.ImmersionBar
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.artist.entity.ArtistInfo
import com.tencent.joox.sdk.business.artist.widget.ArtistFragmentStateAdapter
import com.tencent.joox.sdk.data.entity.PageState
import com.tencent.joox.sdk.databinding.ActivityArtistLayoutBinding

class ArtistActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val KEY_ID = "id"
        private val TITLE_ARRAY = arrayOf("Song", "Album")

        fun toArtistHome(ctx: Context, id: String) {
            val it = Intent(ctx, ArtistActivity::class.java)
            it.putExtra(KEY_ID, id)
            ctx.startActivity(it)
        }
    }

    private lateinit var binding: ActivityArtistLayoutBinding
    private val model: ArtistViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(ArtistViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
        fetch()
        observerPageDataState()
    }

    private fun initUi() {
        fullScreen()
        binding.toolbarHome.ivToolbarBack.visibility = View.VISIBLE
        binding.toolbarHome.ivToolbarBack.setOnClickListener(this)
        binding.toolbarHome.ivToolbarMenu.visibility = View.GONE
        binding.artistHomeError.errorPage.setOnClickListener(this)
        val artistId = intent.getStringExtra(KEY_ID) ?:""
        binding.searchTabContainer.adapter = ArtistFragmentStateAdapter(artistId, this)
        TabLayoutMediator(binding.searchTabBar, binding.searchTabContainer) { tab, position ->
            tab.text = TITLE_ARRAY[position]
        }.attach()
    }

    private fun fullScreen() {
        ImmersionBar.with(this)
                .fullScreen(true)
                .navigationBarColor(android.R.color.transparent)
                .navigationBarDarkIcon(true)
                .init()
    }


    private fun bindUserInfo(info: ArtistInfo?) {
        binding.userName.text = info?.name
        Glide.with(this).load(info?.cover?.first()?.url ?: "").into(binding.userCoverImg)
    }

    private fun fetch() {
        intent.getStringExtra(KEY_ID)?.let {
            model.load(it)
        }
    }


    private fun observerPageDataState() {
        model.observerPageDataState(
            this
        ) {
            when (it.state) {
                PageState.SUCCESS -> {
                    binding.artistHomeError.errorPage.visibility = View.GONE
                    binding.artistHome.visibility = View.VISIBLE
                    bindUserInfo(it.rsp)
                }
                else -> {
                    binding.artistHome.visibility = View.GONE
                    binding.artistHomeError.errorPage.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_toolbar_back -> {
                finish()
            }
            R.id.error_page -> {
                fetch()
            }
        }
    }

}