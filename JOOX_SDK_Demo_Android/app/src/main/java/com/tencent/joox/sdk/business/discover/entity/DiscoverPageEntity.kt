package com.tencent.joox.sdk.business.discover.entity

import com.tencent.joox.sdk.data.entity.DiscoverSectionEntity
import com.tencent.joox.sdk.data.entity.PageEntity
import com.tencent.joox.sdk.data.entity.PageState

class DiscoverPageEntity(state: PageState, rsp: ArrayList<DiscoverSectionEntity>?) :
    PageEntity<ArrayList<DiscoverSectionEntity>>(state, rsp) {
}