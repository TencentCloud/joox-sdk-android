package com.tencent.joox.sdk.tools

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.joox.sdklibrary.AuthState
import com.joox.sdklibrary.AuthType
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.SDKListener
import com.tencent.joox.sdk.NavigateActivity
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.business.subscribe.SubscribeManager
import com.tencent.joox.sdk.databinding.ActivitySingInBinding

class SingInActivity : AppCompatActivity(), View.OnClickListener, SDKListener {
    private var commitResult = false
    private lateinit var binding: ActivitySingInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        SDKInstance.getIns().registerListener(this)
    }

    private fun initView() {
        fullScreen()
        binding.loginWithMobile.setOnClickListener(this)
        binding.loginWithEmail.setOnClickListener(this)
        binding.loginWithQr.setOnClickListener(this)
    }

    private fun fullScreen() {
        ImmersionBar.with(this)
                .fullScreen(true)
                .navigationBarColor(android.R.color.transparent)
                .navigationBarDarkIcon(true)
                .init()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_with_mobile -> {
                SDKInstance.getIns().auth(AuthType.AUTH_WITH_MOBILE)
            }
            R.id.login_with_qr -> {
                SDKInstance.getIns().auth(AuthType.AUTH_WITH_QRCODE)
            }
            R.id.login_with_email -> {
                SDKInstance.getIns().auth(AuthType.AUTH_WITH_EMAIL)
            }
        }
    }

    override fun currentAuthState(authState: Int, errCode: Int, errMsg: String?) {
        if (authState == AuthState.SUCCESS) {
            val intent = Intent()
            intent.setClass(this, NavigateActivity::class.java)
            startActivity(intent)
            SubscribeManager.refresh()
            finish()
        } else if (authState == AuthState.INITED) {
            Toast.makeText(this, "Login fail code:$authState", Toast.LENGTH_LONG)
        }
    }

    override fun updateCurrentPlayState(playState: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        SDKInstance.getIns().unregisterListener(this)
    }
}