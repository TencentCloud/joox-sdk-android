package com.tencent.joox.sdk.business.songlist

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.debug.EnvManager
import com.joox.sdklibrary.kernel.network.SceneBase
import com.tencent.joox.sdk.business.songlist.entity.AlbumEntity
import com.tencent.joox.sdk.business.songlist.entity.MusicPlayListEntity
import com.tencent.joox.sdk.business.songlist.entity.MusicPlayListPage
import com.tencent.joox.sdk.business.songlist.entity.MusicPlayListType
import com.tencent.joox.sdk.business.songlist.entity.MusicRecommendPlayListRsp
import com.tencent.joox.sdk.business.songlist.entity.MusicTopPlayListRsp
import com.tencent.joox.sdk.business.songlist.entity.UserMusicPlayListRsp
import com.tencent.joox.sdk.business.subscribe.SubscribeManager
import com.tencent.joox.sdk.data.entity.PageIndex
import com.tencent.joox.sdk.data.entity.PageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class MusicPlayListViewModel : ViewModel() {

    companion object {
        private const val TAG = "MusicPlayListViewModel"

        private const val ALBUM_METHOD = "v1/album/{id}/tracks"
        private const val TOP_LIST_DETAIL_METHOD = "v1/toplist_detail"
        private const val PLAYLIST_METHOD = "v1/playlist/{id}/tracks"
        private const val USER_METHOD = "v1/user_playlist/tracks"
        private const val USER_FAVORITE_METHOD = "v1/user/me/favorite"
        private const val LIST_COUNT = 10
    }

    private val musicList = MutableLiveData<MusicPlayListPage>()


    fun load(type: Int, id: String = "") {
        Log.d(TAG, "load:${type}, id${id}")
        when (type) {
            MusicPlayListType.USER_FAV_PLAYLIST.type -> {
                loadFavPlayList()
            }
            else -> {
                requestPlayList(type, id)
            }
        }
    }

    private fun loadFavPlayList() {
        SubscribeManager.loadLikeSongList { success, musicListRsp ->
            if (success) {
                if (musicListRsp?.tracks?.items?.isEmpty() != false) {
                    musicList.value = MusicPlayListPage(PageState.EMPTY, null, PageIndex())
                } else {
                    musicListRsp.tracks.let {
                        val nextIndex = it.next_index
                        val listCount = it.items?.size ?: 0
                        val totalCount = it.total_count
                        val pageIndex = PageIndex(nextIndex, listCount, totalCount)
                        val playList = MusicPlayListEntity.generateFavMusicPlayList(musicListRsp)
                        musicList.value = MusicPlayListPage(PageState.SUCCESS, playList, pageIndex)
                    }
                }

            } else {
                Log.d(TAG, "loadFavPlayList fail")
                musicList.value = MusicPlayListPage(PageState.ERROR, null, PageIndex())
            }
        }
    }

    private fun requestPlayList(type: Int, id: String = "") {
        var requestMethod = buildMethod(type, id)
        var params = buildParams(type, id)
        SDKInstance.getIns().doJooxRequest(requestMethod, params, object : SceneBase.OnSceneBack {
            override fun onSuccess(responseCode: Int, jsonResult: String?) {
                Log.d(TAG, "onSuccess:${jsonResult}")
                GlobalScope.launch {
                    val musicListRsp = handleRsp(type, jsonResult ?: "")
                    withContext(Dispatchers.Main) {
                        if (musicListRsp?.tracks?.items?.isEmpty() != false) {
                            musicList.value = MusicPlayListPage(PageState.EMPTY, musicListRsp, PageIndex())
                        } else {
                            val nextIndex = musicListRsp.tracks.next_index
                            val listCount = musicListRsp.tracks.list_count
                            val totalCount = musicListRsp.tracks.total_count
                            val pageIndex = PageIndex(nextIndex, listCount, totalCount)
                            musicList.value = MusicPlayListPage(PageState.SUCCESS, musicListRsp, pageIndex)
                        }
                    }
                }
            }

            override fun onFail(errCode: Int) {
                Log.d(TAG, "errCode:${errCode}")
                musicList.value = MusicPlayListPage(PageState.ERROR, null, PageIndex())
            }

        })
    }

    private fun handleRsp(type: Int, jsonResult: String): MusicPlayListEntity? {
        return when (type) {
            MusicPlayListType.EDITOR_TOP_LIST.type -> {
                val sectionRsp = Gson().fromJson(jsonResult, MusicTopPlayListRsp::class.java)
                MusicPlayListEntity.generateMusicPlayList(sectionRsp)
            }
            MusicPlayListType.EDITOR_RECOMMEND_ALBUM.type,
            MusicPlayListType.ML_RECOMMEND_ALBUM.type -> {
                val sectionRsp = Gson().fromJson(jsonResult, AlbumEntity::class.java)
                MusicPlayListEntity.generateMusicPlayList(sectionRsp)
            }
            MusicPlayListType.USER_SUBSCRIBE_PLAYLIST.type -> {
                val sectionRsp = Gson().fromJson(jsonResult, UserMusicPlayListRsp::class.java)
                MusicPlayListEntity.generateUserMusicPlayList(sectionRsp)
            }
            else -> {
                val sectionRsp = Gson().fromJson(jsonResult, MusicRecommendPlayListRsp::class.java)
                MusicPlayListEntity.generateMusicPlayList(sectionRsp)
            }
        }
    }

    private fun buildMethod(type: Int, id: String): String {
        return when (type) {
            MusicPlayListType.USER_SUBSCRIBE_PLAYLIST.type -> {
                USER_METHOD
            }
            MusicPlayListType.USER_FAV_PLAYLIST.type -> {
                USER_FAVORITE_METHOD
            }
            MusicPlayListType.EDITOR_TOP_LIST.type -> {
                TOP_LIST_DETAIL_METHOD
            }
            MusicPlayListType.EDITOR_RECOMMEND_PLAYLIST.type,
            MusicPlayListType.ML_RECOMMEND_PLAYLIST.type,
            MusicPlayListType.ML_MULTIPLE_SINGER_PLAYLIST.type -> {
                PLAYLIST_METHOD.replace("{id}", id, true)
            }
            MusicPlayListType.EDITOR_RECOMMEND_ALBUM.type,
            MusicPlayListType.ML_RECOMMEND_ALBUM.type -> {
                ALBUM_METHOD.replace("{id}", id, true)
            }
            else -> {
                PLAYLIST_METHOD.replace("{id}", id, true)
            }
        }
    }

    private fun buildParams(type: Int, id: String): String {
        val params = "country=${EnvManager.getCountry()}&lang=${EnvManager.getLanguage()}"
        return when (type) {
            MusicPlayListType.USER_SUBSCRIBE_PLAYLIST.type -> {
                "${params}&id=${URLEncoder.encode(id)}&num=${LIST_COUNT}&index=${musicList.value?.pageIndex?.nextIndex ?: 0}"
            }
            MusicPlayListType.EDITOR_TOP_LIST.type -> {
                "${params}&list_id=${id}"
            }
            MusicPlayListType.USER_FAV_PLAYLIST.type,
            MusicPlayListType.EDITOR_RECOMMEND_PLAYLIST.type,
            MusicPlayListType.ML_RECOMMEND_PLAYLIST.type,
            MusicPlayListType.ML_MULTIPLE_SINGER_PLAYLIST.type,
            MusicPlayListType.EDITOR_RECOMMEND_ALBUM.type,
            MusicPlayListType.ML_RECOMMEND_ALBUM.type -> {
                "${params}&num=${LIST_COUNT}&index=${musicList.value?.pageIndex?.nextIndex ?: 0}"
            }
            else -> {
                "${params}&num=${LIST_COUNT}&index=${musicList.value?.pageIndex?.nextIndex ?: 0}"
            }
        }
    }


    fun observerPageDataState(owner: LifecycleOwner, observer: Observer<MusicPlayListPage>) {
        musicList.observe(owner, observer)
    }

}