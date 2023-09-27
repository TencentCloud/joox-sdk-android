package com.tencent.joox.sdk.business.search.entity

import com.tencent.joox.sdk.data.entity.PageEntity
import com.tencent.joox.sdk.data.entity.PageState

class SearchHintPageEntity(state: PageState, rsp: ArrayList<SearchHintItem>?) :
    PageEntity<ArrayList<SearchHintItem>>(state, rsp) {
}