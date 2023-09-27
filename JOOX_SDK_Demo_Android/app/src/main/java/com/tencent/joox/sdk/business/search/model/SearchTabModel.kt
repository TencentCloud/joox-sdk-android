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

class SearchTabModel : ViewModel() {

    companion object {
        private const val TAG = "SearchTabModel"
        private const val method = "v2/search_type"
        private val params = "${EnvManager.getCountry()}&lang=${EnvManager.getLanguage()}"
    }

    private val tabData = MutableLiveData<SearchTabPageEntity>()

    fun doSearch(type: SearchTab, keyWord: String) {
        val nextIndex = tabData.value?.pageIndex?.nextIndex  ?: 0
        val params = "$params&key=${keyWord}&index=$nextIndex&Type=${type.type}"
        SDKInstance.getIns().doJooxRequest(method, params, object : SceneBase.OnSceneBack {
            override fun onSuccess(responseCode: Int, jsonResult: String?) {
                Log.d(TAG, "onSuccess:${jsonResult}")
                GlobalScope.launch {
                    val searchRsp = Gson().fromJson(jsonResult, SearchTabRsp::class.java)
                    withContext(Dispatchers.Main) {
                        val searchItemList = coverItem(type, searchRsp)
                        if (searchItemList.isEmpty()) {
                            tabData.value = SearchTabPageEntity(PageState.EMPTY, null, PageIndex())
                        } else {
                            val nextIndex = searchRsp.nextPage
                            tabData.value = SearchTabPageEntity(
                                PageState.SUCCESS,
                                searchItemList,
                                PageIndex(nextIndex, searchItemList.size, 0, searchRsp.hasMore)
                            )
                        }
                    }
                }
            }

            override fun onFail(errCode: Int) {
                Log.d(TAG, "errCode:${errCode}")
                tabData.value = SearchTabPageEntity(PageState.ERROR, null, PageIndex())
            }

        })
    }

    private fun coverItem(type: SearchTab, searchRsp: SearchTabRsp?): ArrayList<SearchTabItem> {
        val dataList = ArrayList<SearchTabItem>()
        when (type) {
            SearchTab.SONG -> {
                searchRsp?.tracks?.forEach {
                    dataList.add(SearchTabItem(SearchTab.SONG, it))
                }
            }
            SearchTab.ARTIST -> {
                searchRsp?.artists?.forEach {
                    dataList.add(SearchTabItem(SearchTab.ARTIST, it))
                }
            }
            SearchTab.ALBUM -> {
                searchRsp?.albums?.forEach {
                    dataList.add(SearchTabItem(SearchTab.ALBUM, it))
                }
                searchRsp?.albums?.isEmpty() ?: true
            }
            SearchTab.PLAYLIST -> {
                searchRsp?.playlists?.forEach {
                    dataList.add(SearchTabItem(SearchTab.PLAYLIST, it))
                }
            }

        }
        return dataList

    }

    fun observerPageDataState(owner: LifecycleOwner, observer: Observer<SearchTabPageEntity>) {
        tabData.observe(owner, observer)
    }

}