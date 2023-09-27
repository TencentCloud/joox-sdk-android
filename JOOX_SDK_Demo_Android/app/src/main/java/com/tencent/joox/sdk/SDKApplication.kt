package com.tencent.joox.sdk

import android.app.Application
import android.os.Build
import android.util.Log
import com.joox.sdklibrary.AuthType
import com.joox.sdklibrary.SDKInstance
import com.tencent.joox.sdk.data.ConstKey
import com.tencent.joox.sdk.tools.SharedPreferencesTool
import kotlin.properties.Delegates

class SDKApplication : Application() {


    companion object {
        var instance: SDKApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("SDKApplication", "onCreate called!!!!");
        instance = this
        val scopeList = arrayListOf(getScopeList())
        val appKey = "input app key"
        val appPkg = "input app package name"
        SDKInstance.getIns().init(instance, appKey, appPkg, scopeList)
    }

    override fun onTerminate() {
        super.onTerminate()
        SDKInstance.getIns().logout();
    }

    fun saveScopeList(scopeList: String) {
        SharedPreferencesTool.getmInstance(applicationContext).commitStringValue(ConstKey.SCOPE_KEY, scopeList)
    }

    fun getScopeList(): String {
        return SharedPreferencesTool.getmInstance(applicationContext)
                .getStringValue(ConstKey.SCOPE_KEY, "public+user_profile+playmusic+search+user_music")
    }

}