package com.tencent.joox.sdk.business.discover

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.debug.EnvManager
import com.joox.sdklibrary.kernel.network.SceneBase
import com.tencent.joox.sdk.business.discover.entity.DiscoverPageEntity
import com.tencent.joox.sdk.data.entity.DiscoverSectionRspEntity
import com.tencent.joox.sdk.data.entity.PageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscoverViewModel : ViewModel() {

    companion object {
        private const val TAG = "DiscoverViewModel"
        private const val SERVICE = "v1/discover"
    }
    private val discoverData = MutableLiveData<DiscoverPageEntity>()

    fun load() {
        val params = "country=${EnvManager.getCountry()}&lang=${EnvManager.getLanguage()}"
        SDKInstance.getIns().doJooxRequest(SERVICE, params, object : SceneBase.OnSceneBack {
            override fun onSuccess(responseCode: Int, JsonResult: String?) {
                Log.d(TAG, "onSuccess:${JsonResult}")
                GlobalScope.launch {
                    val sectionRsp = Gson().fromJson(JsonResult, DiscoverSectionRspEntity::class.java)
                    withContext(Dispatchers.Main){
                        val state = if (sectionRsp?.itemList?.isEmpty() != false) PageState.EMPTY else PageState.SUCCESS
                        discoverData.value = DiscoverPageEntity(state, sectionRsp.itemList)
                    }
                }
            }

            override fun onFail(errCode: Int) {
                Log.d(TAG, "errCode:${errCode}")
                discoverData.value = DiscoverPageEntity(PageState.ERROR, null)
            }

        })
    }

    fun observerPageDataState(owner: LifecycleOwner, observer: Observer<DiscoverPageEntity>) {
        discoverData.observe(owner, observer)
    }

}