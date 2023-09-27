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
import com.tencent.joox.sdk.business.artist.entity.ArtistAlbumTabData
import com.tencent.joox.sdk.business.artist.entity.ArtistAlbumTabPage
import com.tencent.joox.sdk.data.entity.PageIndex
import com.tencent.joox.sdk.data.entity.PageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistAlbumTabModel : ViewModel() {

    companion object {
        private const val TAG = "ArtistSongTabModel"
        private const val METHOD = "v1/artist/{id}/albums"
        private val params = "&country=${EnvManager.getCountry()}&lang=${EnvManager.getLanguage()}"
    }

    private val tabData = MutableLiveData<ArtistAlbumTabPage>()

    fun load(id: String) {
        val nextIndex = tabData.value?.pageIndex?.nextIndex ?: 0
        val params = "$params&num=20&index=$nextIndex&id=${id}"
        SDKInstance.getIns().doJooxRequest(METHOD.replace("{id}", id), params, object : SceneBase.OnSceneBack {
            override fun onSuccess(responseCode: Int, jsonResult: String?) {
                Log.d(TAG, "onSuccess:${jsonResult}")
                GlobalScope.launch {
                    val rspData = Gson().fromJson(jsonResult, ArtistAlbumTabData::class.java)
                    withContext(Dispatchers.Main) {
                        if (rspData.albums?.items?.isEmpty() == true) {
                            tabData.value = ArtistAlbumTabPage(PageState.EMPTY, null, PageIndex())
                        } else {
                            val pageIndex = PageIndex(
                                rspData.albums?.nextIndex ?: 0,
                                rspData.albums?.listCount ?: 0,
                                rspData?.albums?.totalCount ?: 0
                            )
                            tabData.value = ArtistAlbumTabPage(PageState.SUCCESS, rspData, pageIndex)
                        }
                    }
                }
            }

            override fun onFail(errCode: Int) {
                Log.d(TAG, "errCode:${errCode}")
                tabData.value = ArtistAlbumTabPage(PageState.ERROR, null, PageIndex())
            }

        })
    }


    fun observerPageDataState(owner: LifecycleOwner, observer: Observer<ArtistAlbumTabPage>) {
        tabData.observe(owner, observer)
    }

}