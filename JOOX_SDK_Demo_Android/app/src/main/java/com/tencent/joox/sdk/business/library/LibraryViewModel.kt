package com.tencent.joox.sdk.business.library

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.tencent.joox.sdk.business.library.entity.LibraryPageEntity
import com.tencent.joox.sdk.business.subscribe.LikeContentType
import com.tencent.joox.sdk.business.subscribe.SubscribeChangeListener
import com.tencent.joox.sdk.business.subscribe.SubscribeManager
import com.tencent.joox.sdk.business.subscribe.entity.SubscribePlayListEntity
import com.tencent.joox.sdk.data.entity.PageState

class LibraryViewModel : ViewModel(), SubscribeChangeListener {

    private val subscribePlayListData = MutableLiveData<LibraryPageEntity>()

    init {
        SubscribeManager.addLikeChangeListener(this)
    }

    fun load() {
        SubscribeManager.loadLikePlayList { success, playList ->
            if (success) {
                val pageState = if (playList?.isNotEmpty() == true) PageState.SUCCESS else PageState.EMPTY
                subscribePlayListData.value = LibraryPageEntity(pageState, playList)
            }else{
                subscribePlayListData.value = LibraryPageEntity(PageState.ERROR, null)
            }
        }
    }

    fun observerSubscribePlayListDataState(owner: LifecycleOwner, observer: Observer<LibraryPageEntity>) {
        subscribePlayListData.observe(owner, observer)
    }

    override fun onSubscribeChanged(isLike: Boolean, contentType: LikeContentType, id: String) {
       when(contentType){
           LikeContentType.PLAY_LIST ->{
               load()
           }
       }
    }

}