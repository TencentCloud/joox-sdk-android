package com.tencent.joox.sdk.business.search.model

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.debug.EnvManager
import com.joox.sdklibrary.kernel.network.SceneBase
import com.tencent.joox.sdk.business.search.entity.SearchAllPageEntity
import com.tencent.joox.sdk.business.search.entity.SearchAllRsp
import com.tencent.joox.sdk.business.search.entity.SearchTab
import com.tencent.joox.sdk.business.search.entity.SearchTabItem
import com.tencent.joox.sdk.business.search.entity.SearchTabPageEntity
import com.tencent.joox.sdk.business.search.entity.SearchTabRsp
import com.tencent.joox.sdk.data.entity.PageIndex
import com.tencent.joox.sdk.data.entity.PageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus

class SearchAllModel : ViewModel() {

    companion object {
        private const val TAG = "SearchAllModel"
        private const val method = "v3/search"
        private val params = "${EnvManager.getCountry()}&lang=${EnvManager.getLanguage()}"
    }

    private val tabData = MutableLiveData<SearchAllPageEntity>()

    fun doSearch(type: SearchTab, keyWord: String) {
        val params = "$params&keyword=${keyWord}&start_index=0&end_index=2  0"
        SDKInstance.getIns().doJooxRequest(method, params, object : SceneBase.OnSceneBack {
            override fun onSuccess(responseCode: Int, jsonResult: String?) {
                Log.d(TAG, "onSuccess:${jsonResult}")
                GlobalScope.launch {
                    val searchRsp = Gson().fromJson(jsonResult, SearchAllRsp::class.java)
                    searchRsp?.sectionList?.removeIf { it.sectionType == 6 || it.sectionType ==7 }
                    searchRsp?.sectionList?.forEach {sectionInfo ->
                        sectionInfo.itemList?.removeIf { itemInfo ->
                            itemInfo.type ==8 || itemInfo.type ==9
                        }
                    }
                    withContext(Dispatchers.Main) {
                        if (searchRsp?.sectionList?.isEmpty() == true) {
                            tabData.value = SearchAllPageEntity(PageState.EMPTY, null)
                        } else {
                            tabData.value = SearchAllPageEntity(PageState.SUCCESS,searchRsp?.sectionList)
                        }
                    }
                }
            }

            override fun onFail(errCode: Int) {
                Log.d(TAG, "errCode:${errCode}")
                tabData.value = SearchAllPageEntity(PageState.ERROR, null)
            }

        })
    }


    fun observerPageDataState(owner: LifecycleOwner, observer: Observer<SearchAllPageEntity>) {
        tabData.observe(owner, observer)
    }

}