package com.tencent.joox.sdk.business.subscribe

import android.util.Log
import com.google.gson.Gson
import com.joox.sdklibrary.SDKInstance
import com.joox.sdklibrary.debug.EnvManager
import com.joox.sdklibrary.kernel.network.SceneBase
import com.tencent.joox.sdk.business.subscribe.entity.SubscribeActionEntity
import com.tencent.joox.sdk.business.subscribe.entity.SubscribePlayListEntity
import com.tencent.joox.sdk.business.subscribe.entity.SubscribeSongEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

object SubscribeManager {

    private const val TAG = "SubscribeManager"

    val likeSongList = arrayListOf<String>()
    val likeMusicPLayList = arrayListOf<String>()
    private val subscribeChangeListener = arrayListOf<SubscribeChangeListener>()

    fun refresh() {
        SDKInstance.getIns().userManager.userInfo?.let {
            loadLikeSongList()
            loadLikePlayList()
        }
    }

    fun isSongSubscribed(songId: String): Boolean {
        return likeSongList.contains(songId)
    }

    fun isMusicPlayListSubscribed(playListId: String): Boolean {
        return likeMusicPLayList.contains(playListId)
    }


    fun addLikeChangeListener(listener: SubscribeChangeListener) {
        subscribeChangeListener.add(listener)
    }

    fun removeLikeChangeListener(listener: SubscribeChangeListener) {
        subscribeChangeListener.remove(listener)
    }

    fun doLikeSong(isLike: Boolean, songId: String, callback: ((success: Boolean, status: Int) -> Unit)? = null) {
        val method = "v1/song_list/subscribe"
        val isSubscribe = if (isLike) 1 else 0
        val params ="sub_id=${URLEncoder.encode(songId)}" +
                "&country=${EnvManager.getCountry()}" +
                "&lang=${EnvManager.getLanguage()}" +
                "&is_subscribe=${isSubscribe}&list_type=5"
        SDKInstance.getIns().doJooxRequest(method, params, object : SceneBase.OnSceneBack {
            override fun onSuccess(responseCode: Int, JsonResult: String?) {
                Log.d(TAG, "doLikeSong onSuccess:${JsonResult}")
                GlobalScope.launch {
                    val songLikeRsp = Gson().fromJson(JsonResult, SubscribeActionEntity::class.java)
                    withContext(Dispatchers.Main) {
                        updateLikeSongList(songLikeRsp.subscribeStatus == 1, songId)
                        callback?.invoke(true, songLikeRsp.subscribeStatus)
                    }
                }
            }

            override fun onFail(errCode: Int) {
                callback?.invoke(false, 0)
                Log.d(TAG, "doLikeSong errCode:${errCode}")
            }
        })
    }

    fun loadLikeSongList(callback: ((success: Boolean, playList: SubscribeSongEntity?) -> Unit)? = null) {
        val method = "v1/user/me/favorite"
        val params = "country=${EnvManager.getCountry()}&lang=${EnvManager.getLanguage()}"
        SDKInstance.getIns().doJooxRequest(method, params, object : SceneBase.OnSceneBack {
            override fun onSuccess(responseCode: Int, JsonResult: String?) {
                Log.d(TAG, "loadLikeSongList onSuccess:${JsonResult}")
                GlobalScope.launch {
                    val songLikeRsp = Gson().fromJson(JsonResult, SubscribeSongEntity::class.java)
                    songLikeRsp.tracks?.items?.forEach { trackItem ->
                        likeSongList.add(trackItem.id)
                    }
                    withContext(Dispatchers.Main) {
                        callback?.invoke(true, songLikeRsp)
                    }
                }
            }

            override fun onFail(errCode: Int) {
                Log.d(TAG, "loadLikeSongList errCode:${errCode}")
                callback?.invoke(false, null)
            }
        })
    }

    private fun updateLikeSongList(isLike: Boolean, songId: String) {
        if (isLike) {
            likeSongList.add(songId)
        } else {
            likeSongList.remove(songId)
        }
        subscribeChangeListener.forEach {
            it.onSubscribeChanged(isLike, LikeContentType.SONG, songId)
        }
    }


    fun doLikeMusicPlayList(
        isLike: Boolean,
        id: String,
        musicPlayListId: String,
        listType: Int,
        callback: ((success: Boolean, status: Int) -> Unit)? = null
    ) {
        val method = "v1/song_list/subscribe"
        val isSubscribe = if (isLike) 1 else 0
        val params ="id=${id}&sub_id=${URLEncoder.encode(musicPlayListId)}" +
                "&country=${EnvManager.getCountry()}" +
                "&lang=${EnvManager.getLanguage()}" +
                "&is_subscribe=${isSubscribe}" +
                "&list_type=0"
        SDKInstance.getIns().doJooxRequest(method, params, object : SceneBase.OnSceneBack {
            override fun onSuccess(responseCode: Int, JsonResult: String?) {
                Log.d(TAG, "doLikeMusicPlayList onSuccess:${JsonResult}")
                GlobalScope.launch {
                    val songLikeRsp = Gson().fromJson(JsonResult, SubscribeActionEntity::class.java)
                    withContext(Dispatchers.Main) {
                        callback?.invoke(true, songLikeRsp.subscribeStatus)
                        updateLikePlayList(songLikeRsp.subscribeStatus == 1, musicPlayListId)
                    }
                }
            }

            override fun onFail(errCode: Int) {
                callback?.invoke(false, 0)
                Log.d(TAG, "doLikeMusicPlayList errCode:${errCode}")
            }
        })
    }

    fun loadLikePlayList(callback: ((success: Boolean, playList: ArrayList<SubscribePlayListEntity.SubscribePlayList>?) -> Unit)? = null) {
        val method = "v1/song_list/get_subscribed"
        val params = "country=${EnvManager.getCountry()}&lang=${EnvManager.getLanguage()}"
        SDKInstance.getIns().doJooxRequest(method, params, object : SceneBase.OnSceneBack {
            override fun onSuccess(responseCode: Int, JsonResult: String?) {
                Log.d(TAG, "loadLikePlayList onSuccess:${JsonResult}")
                GlobalScope.launch {
                    val songLikeRsp = Gson().fromJson(JsonResult, SubscribePlayListEntity::class.java)
                    songLikeRsp.playList?.forEach { playList ->
                        likeMusicPLayList.add(playList.subId)
                    }
                    withContext(Dispatchers.Main) {
                        callback?.invoke(true, songLikeRsp.playList)
                    }
                }
            }

            override fun onFail(errCode: Int) {
                Log.d(TAG, "loadLikePlayList errCode:${errCode}")
                callback?.invoke(false, null)
            }
        })
    }

    private fun updateLikePlayList(isLike: Boolean, songId: String) {
        if (isLike) {
            likeMusicPLayList.add(songId)
        } else {
            likeMusicPLayList.remove(songId)
        }
        subscribeChangeListener.forEach {
            it.onSubscribeChanged(isLike, LikeContentType.PLAY_LIST, songId)
        }
    }
}