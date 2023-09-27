package com.tencent.joox.sdk.business.search.entity

import com.tencent.joox.sdk.data.entity.PageEntity
import com.tencent.joox.sdk.data.entity.PageState


class SearchLandingPageEntity(state: PageState, rsp: ArrayList<SearchLandingSectionEntity>?) :
    PageEntity<ArrayList<SearchLandingSectionEntity>>(state, rsp) {
}

data class SearchLandingSectionEntity(
    val title: String = "",
    val type: SearchLandingType = SearchLandingType.OTHER,
    val itemList: ArrayList<*>? = null
)
enum class SearchLandingType {
    OTHER, HOTWORD, SINGER, PLAYLIST, ALBUM, TRACT, PROMPT
}