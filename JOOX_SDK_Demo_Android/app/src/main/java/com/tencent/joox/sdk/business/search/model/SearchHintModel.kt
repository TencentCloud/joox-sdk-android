package com.tencent.joox.sdk.business.search.model

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.debug.EnvManager
import com.joox.sdklibrary.kernel.network.SceneBase
import com.tencent.joox.sdk.business.search.entity.SearchHintItem
import com.tencent.joox.sdk.business.search.entity.SearchHintPageEntity
import com.tencent.joox.sdk.business.search.entity.SearchHintRsp
import com.tencent.joox.sdk.business.search.entity.SearchKey
import com.tencent.joox.sdk.data.entity.PageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SearchHintModel : ViewModel() {

    companion object {
        private const val TAG = "SearchHintModel"
        private const val method = "v3/search_hint"
    }

    private val hintData = MutableLiveData<SearchHintPageEntity>()


    fun init(keyWord: String) {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        doSearch(keyWord)
    }

    fun doSearch(keyWord: String) {
        val params = "country=${EnvManager.getCountry()}&lang=${EnvManager.getLanguage()}&keyword=${keyWord}"
        SDKInstance.getIns().doJooxRequest(method, params, object : SceneBase.OnSceneBack {
            override fun onSuccess(responseCode: Int, jsonResult: String?) {
                Log.d(TAG, "onSuccess:${jsonResult}")
                GlobalScope.launch {
                    val searchRsp = Gson().fromJson(jsonResult, SearchHintRsp::class.java)
                    val hintEntityList = arrayListOf<SearchHintItem>()
                    searchRsp.items?.forEach { itemRsp ->
                        hintEntityList.add(itemRsp)
                    }
                    withContext(Dispatchers.Main) {
                        if (hintEntityList.isEmpty()) {
                            hintData.value = SearchHintPageEntity(PageState.EMPTY, null)
                        } else {
                            hintData.value = SearchHintPageEntity(PageState.SUCCESS, hintEntityList)
                        }
                    }
                }
            }

            override fun onFail(errCode: Int) {
                Log.d(TAG, "errCode:${errCode}")
                hintData.value = SearchHintPageEntity(PageState.ERROR, null)
            }

        })
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSearchChanged(key: SearchKey) {
        if (TextUtils.isEmpty(key.word)){
            hintData.value = SearchHintPageEntity(PageState.EMPTY, null)
        }else{
            doSearch(key.word)
        }
    }

    fun observerPageDataState(owner: LifecycleOwner, observer: Observer<SearchHintPageEntity>) {
        hintData.observe(owner, observer)
    }

    fun unInit() {
        EventBus.getDefault().unregister(this)
    }
}