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
import com.tencent.joox.sdk.business.search.entity.SearchLandingPageEntity
import com.tencent.joox.sdk.business.search.entity.SearchLandingSectionEntity
import com.tencent.joox.sdk.business.search.entity.SearchLandingSectionRsp
import com.tencent.joox.sdk.business.search.entity.SearchLandingType
import com.tencent.joox.sdk.data.entity.PageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchLandingViewModel : ViewModel() {

    companion object {
        private const val TAG = "SearchLandingViewModel"
        private const val SERVICE = "v2/search_landing_page"
        private const val HOT_WORD_SECTION_TITLE = "Trending Searches"
        private const val ARTISTS_TITLE = "Artists"
        private const val RECOMMENDED_PLAYLISTS_TITLE = "Recommended Playlists"
    }
    private val discoverData = MutableLiveData<SearchLandingPageEntity>()

    fun load() {
        val openId = SDKInstance.getIns().userManager.userInfo?.openId ?: ""
        val params = "country=${EnvManager.getCountry()}&lang=${EnvManager.getLanguage()}&openid=${openId}"
        SDKInstance.getIns().doJooxRequest(SERVICE, params, object : SceneBase.OnSceneBack {
            override fun onSuccess(responseCode: Int, JsonResult: String?) {
                Log.d(TAG, "onSuccess:${JsonResult}")
                GlobalScope.launch {
                    val sectionRsp = Gson().fromJson(JsonResult, SearchLandingSectionRsp::class.java)
                    withContext(Dispatchers.Main){
                        if (sectionRsp.isEmpty()){
                            discoverData.value = SearchLandingPageEntity(PageState.EMPTY, null)
                        }else{
                            val sectionList = arrayListOf<SearchLandingSectionEntity>()
                            val hotWordList = sectionRsp.hotWordEntityList
                            if (hotWordList?.isNotEmpty() == true){
                                sectionList.add(SearchLandingSectionEntity(HOT_WORD_SECTION_TITLE, SearchLandingType.HOTWORD, hotWordList))
                            }
                            val singerList = sectionRsp.singerList
                            if (singerList?.isNotEmpty() == true){
                                sectionList.add(SearchLandingSectionEntity(ARTISTS_TITLE, SearchLandingType.SINGER, singerList))
                            }
                            val playList = sectionRsp.playList
                            if (playList?.isNotEmpty() == true){
                                sectionList.add(SearchLandingSectionEntity(RECOMMENDED_PLAYLISTS_TITLE, SearchLandingType.PLAYLIST, playList))
                            }
                            discoverData.value = SearchLandingPageEntity(PageState.SUCCESS, sectionList)

                        }
                    }
                }
            }

            override fun onFail(errCode: Int) {
                Log.d(TAG, "errCode:${errCode}")
                discoverData.value = SearchLandingPageEntity(PageState.ERROR, null)
            }

        })
    }

    fun observerPageDataState(owner: LifecycleOwner, observer: Observer<SearchLandingPageEntity>) {
        discoverData.observe(owner, observer)
    }

}