package com.tencent.joox.sdk

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.joox.sdklibrary.AuthState
import com.joox.sdklibrary.AuthType
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.SDKListener
import com.tencent.joox.sdk.business.subscribe.SubscribeManager
import com.tencent.joox.sdk.databinding.ActivityMainBinding
import com.tencent.joox.sdk.tools.SharedPreferencesTool
import com.tencent.joox.sdk.tools.SingInActivity
import com.tencent.joox.sdk.utils.PermissionUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binder: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStoragePermission();
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)
        fullScreen()
        toNext()
        finish()
    }

    private fun toNext() {
        val tokenInfo = SDKInstance.getIns().tokenInfo
        if (tokenInfo == null || TextUtils.isEmpty(tokenInfo.token) || tokenInfo.isTokenNotValid) {
            // 重新登录
            val intent = Intent()
            intent.setClass(this, SingInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
            startActivity(intent)
        } else {
            // 进入主页
            val intent = Intent()
            intent.setClass(this, NavigateNewActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun fullScreen() {
        ImmersionBar.with(this)
                .fullScreen(true)
                .navigationBarColor(android.R.color.transparent)
                .navigationBarDarkIcon(true)
                .init()
    }

    @TargetApi(23)
    fun initStoragePermission() {
        if (Build.VERSION.SDK_INT > 23) {
            requestPermissions(PermissionUtil.permissions, 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
