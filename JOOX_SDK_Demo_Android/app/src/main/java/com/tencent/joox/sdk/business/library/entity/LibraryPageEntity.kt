package com.tencent.joox.sdk.business.library.entity

import com.tencent.joox.sdk.business.subscribe.entity.SubscribePlayListEntity
import com.tencent.joox.sdk.data.entity.DiscoverSectionEntity
import com.tencent.joox.sdk.data.entity.PageEntity
import com.tencent.joox.sdk.data.entity.PageState

class LibraryPageEntity(state: PageState, rsp: ArrayList<SubscribePlayListEntity.SubscribePlayList>?) :
    PageEntity<ArrayList<SubscribePlayListEntity.SubscribePlayList>>(state, rsp) {
}