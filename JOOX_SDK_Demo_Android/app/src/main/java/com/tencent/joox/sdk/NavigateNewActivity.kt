package com.tencent.joox.sdk

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.google.android.material.navigation.NavigationBarView
import com.gyf.immersionbar.ImmersionBar
import com.joox.sdklibrary.SDKInstance
import com.tencent.joox.sdk.business.settings.SettingActivity
import com.tencent.joox.sdk.databinding.ActivityNavigateNewBinding
import com.tencent.joox.sdk.tools.SingInActivity
import com.tencent.joox.sdk.widget.adapter.NavigateFragmentStateAdapter


class NavigateNewActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener, View.OnClickListener {

    companion object{
        private const val DISCOVER = 0
        private const val SEARCH = 1
        private const val LIBRARY = 2
    }

    private lateinit var binding: ActivityNavigateNewBinding
    private val adapter: NavigateFragmentStateAdapter by lazy { NavigateFragmentStateAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigateNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()

    }

    private fun initUi() {
        fullScreen()
        binding.navigateContainer.adapter = adapter
        binding.navigateContainer.orientation = ORIENTATION_HORIZONTAL
        binding.navigateContainer.isUserInputEnabled = false
        binding.navigateBottomBar.setOnItemSelectedListener(this)
        binding.toolbarHome.tvToolbarTitle.text = getString(R.string.discover)
        binding.toolbarHome.ivToolbarMenu.setOnClickListener(this)
    }

    private fun fullScreen() {
        ImmersionBar.with(this)
                .fullScreen(true)
                .navigationBarColor(R.color.navigation_bar_color)
                .navigationBarDarkIcon(true)
                .init()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var position = DISCOVER
        when (item.itemId) {
            R.id.tab_discover -> {
                position = DISCOVER
            }
            R.id.tab_search -> {
                position = SEARCH
            }
            R.id.tab_library -> {
                position = LIBRARY
            }
        }
        binding.navigateContainer.setCurrentItem(position, false)
        binding.toolbarHome.tvToolbarTitle.text = item.title
        return true
    }

    override fun onClick(v: View?) {
       val it = Intent(this, SettingActivity::class.java)
        startActivity(it)
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
    }

    private fun checkLogin() {
        val tokenInfo = SDKInstance.getIns().tokenInfo
        if (tokenInfo == null || TextUtils.isEmpty(tokenInfo.token) || tokenInfo.isTokenNotValid) {
            // 重新登录
            val intent = Intent()
            intent.setClass(this, SingInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
            startActivity(intent)
            finish()
        }
    }

}