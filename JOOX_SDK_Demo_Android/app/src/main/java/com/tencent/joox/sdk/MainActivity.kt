package com.tencent.joox.sdk

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.gson.GsonBuilder
import com.joox.sdklibrary.AuthState
import com.joox.sdklibrary.AuthType
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.SDKListener
import com.joox.sdklibrary.kernel.network.SceneBase
import com.tencent.mars.xlog.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SDKListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStoragePermission();
        setContentView(R.layout.activity_main)
        // Example of a call to a native method
        SDKInstance.getmInstance().registerListener(this)
        sample_text.setText("login via "+ getLoginMethod() )
        debug_tool.setOnClickListener {
            val intent = Intent()
            intent.setClass(this,TestEnvActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
            startActivity(intent)
        }

    }

    @TargetApi(23)
    fun initStoragePermission(){
        if(Build.VERSION.SDK_INT > 23){
            requestPermissions(PermissionUtil.permissions,1)
        }
    }

    fun getLoginMethod(): String {
        val loginType = SharedPreferencesTool.getmInstance(applicationContext)
                .getIntValue(SharedPreferencesTool.LOGIN_TYPE, AuthType.AUTH_WITH_QRCODE)
        return when (loginType) {
            AuthType.AUTH_WITH_MOBILE -> {
                "joox connect"
            }
            AuthType.AUTH_WITH_QRCODE -> {
                "qr"
            }
            AuthType.AUTH_OPENID -> {
                "openId"
            }
            AuthType.AUTH_TICKET_TOKEN -> {
                edit_ticket_token.visibility = View.VISIBLE
                "ticket token"
            }
            else -> "joox connect"
        }
    }

    override fun onResume() {
        super.onResume()
        val  loginType = SharedPreferencesTool.getmInstance(applicationContext)
                .getIntValue(SharedPreferencesTool.LOGIN_TYPE,AuthType.AUTH_WITH_QRCODE)
        sample_text.setOnClickListener {
            if(loginType == AuthType.AUTH_OPENID){
                SDKInstance.mSessionKey = "";
                SDKInstance.mOpenId = "";
                SDKInstance.getmInstance().auth()
            } else if (loginType == AuthType.AUTH_TICKET_TOKEN) {
                SDKInstance.ticketToken = edit_ticket_token.text.toString();
                SDKInstance.getmInstance().auth()
            } else {
                SDKInstance.getmInstance().auth()
            }
        }
    }

    override fun currentAuthState(authState: Int) {
        if (authState == AuthState.SUCCESS) {
            val intent = Intent()
            intent.setClass(this, NavigateActivity::class.java)
            sample_text.text = "Login Already!"
            startActivity(intent)
        }else if(authState == AuthState.INITED){
            sample_text.setText("login via "+ getLoginMethod() )

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SDKInstance.getmInstance().unregisterListener(this)
    }

    override fun updateCurrentPlayState(playState: Int) {
        Log.d("MainActivity", playState.toString())
    }
}
