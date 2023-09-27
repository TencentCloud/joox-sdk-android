package com.tencent.joox.sdk.business.artist.model

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.debug.EnvManager
import com.joox.sdklibrary.kernel.network.SceneBase
import com.tencent.joox.sdk.business.artist.entity.ArtistSongTabData
import com.tencent.joox.sdk.business.artist.entity.ArtistSongTabPage
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

class ArtistSongTabModel : ViewModel() {

    companion object {
        private const val TAG = "ArtistSongTabModel"
        private const val METHOD = "v1/artist/{id}/tracks"
        private val params = "&country=${EnvManager.getCountry()}&lang=${EnvManager.getLanguage()}"
    }

    private val tabData = MutableLiveData<ArtistSongTabPage>()

    fun load(id: String) {
        val nextIndex = tabData.value?.pageIndex?.nextIndex ?: 0
        val params = "$params&num=20&index=$nextIndex&id=${id}"
        SDKInstance.getIns().doJooxRequest(METHOD.replace("{id}", id), params, object : SceneBase.OnSceneBack {
            override fun onSuccess(responseCode: Int, jsonResult: String?) {
                Log.d(TAG, "onSuccess:${jsonResult}")
                GlobalScope.launch {
                    val rspData = Gson().fromJson(jsonResult, ArtistSongTabData::class.java)
                    withContext(Dispatchers.Main) {
                        if (rspData.tracks?.items?.isEmpty() == true) {
                            tabData.value = ArtistSongTabPage(PageState.EMPTY, null, PageIndex())
                        } else {
                            val pageIndex = PageIndex(
                                rspData?.tracks?.next_index ?: 0,
                                rspData.tracks?.list_count ?: 0,
                                rspData?.tracks?.total_count ?: 0
                            )
                            tabData.value = ArtistSongTabPage(PageState.SUCCESS, rspData, pageIndex)
                        }
                    }
                }
            }

            override fun onFail(errCode: Int) {
                Log.d(TAG, "errCode:${errCode}")
                tabData.value = ArtistSongTabPage(PageState.ERROR, null, PageIndex())
            }

        })
    }


    fun observerPageDataState(owner: LifecycleOwner, observer: Observer<ArtistSongTabPage>) {
        tabData.observe(owner, observer)
    }

}