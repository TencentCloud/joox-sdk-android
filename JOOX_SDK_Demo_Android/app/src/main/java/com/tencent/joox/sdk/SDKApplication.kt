package com.tencent.joox.sdk

import android.app.Application
import android.util.Log
import com.joox.sdklibrary.AuthType
import com.joox.sdklibrary.SDKInstance
import com.tencent.ibg.joox.opensdk.openapi.JXApiFactory

import kotlin.properties.Delegates

class SDKApplication : Application() {


    companion object {
        var instance: SDKApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("SDKApplication", "onCreate called!!!!");
        instance = this
        val scopeList = ArrayList<String>()
        scopeList.add(getScopeList())

        JXApiFactory.sDebugLevel = SDKInstance.ENVIR_TYPE
        val isDebugMode = SDKInstance.ENVIR_TYPE != SDKInstance.PUBLISH
        val loginType = SharedPreferencesTool.getmInstance(applicationContext).getIntValue(SharedPreferencesTool.LOGIN_TYPE, AuthType.AUTH_WITH_QRCODE)

        // TODO: 2021/7/17  your {AppId} & {App Package Name}
        SDKInstance.getmInstance().init(instance, null, {ZAppId}, {App Package Name}, loginType, scopeList)
        SDKInstance.mInstance.setDownFileDir(externalCacheDir.absolutePath + "/testsong/")

    }

    override fun onTerminate() {
        super.onTerminate()
        SDKInstance.getmInstance().logout();
    }

    fun saveScopeList(scopeList: String) {
        SharedPreferencesTool.getmInstance(applicationContext).commitStringValue(ConstKey.SCOPE_KEY, scopeList)
    }

    fun getScopeList(): String {
        return SharedPreferencesTool.getmInstance(applicationContext).getStringValue(ConstKey.SCOPE_KEY, "public+user_profile+playmusic_free+search+user_music")
    }

}