package com.tencent.joox.sdk.business.artist

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.debug.EnvManager
import com.joox.sdklibrary.kernel.network.SceneBase
import com.tencent.joox.sdk.business.artist.entity.ArtistHomePage
import com.tencent.joox.sdk.business.artist.entity.ArtistInfo
import com.tencent.joox.sdk.data.entity.PageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistViewModel : ViewModel() {

    companion object {
        private const val TAG = "ArtistViewModel"

        private const val METHOD = "v1/artist/{id}"
    }

    private val musicList = MutableLiveData<ArtistHomePage>()


    fun load(id: String) {
        Log.d(TAG, "load: id${id}")
        requestArtistInfo(id)
    }


    private fun requestArtistInfo(id: String = "") {
        val method = "v1/artist/${id}"
        val params = "country=${EnvManager.getCountry()}&lang=${EnvManager.getLanguage()}"
        SDKInstance.getIns().doJooxRequest(method, params, object : SceneBase.OnSceneBack {
            override fun onSuccess(responseCode: Int, jsonResult: String?) {
                Log.d(TAG, "onSuccess:${jsonResult}")
                GlobalScope.launch {
                    val rsp = Gson().fromJson(jsonResult, ArtistInfo::class.java)
                    withContext(Dispatchers.Main) {
                        musicList.value = ArtistHomePage(PageState.SUCCESS, rsp)
                    }
                }
            }

            override fun onFail(errCode: Int) {
                Log.d(TAG, "errCode:${errCode}")
                musicList.value = ArtistHomePage(PageState.ERROR, null,)
            }

        })
    }



    fun observerPageDataState(owner: LifecycleOwner, observer: Observer<ArtistHomePage>) {
        musicList.observe(owner, observer)
    }

}