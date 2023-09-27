package com.tencent.joox.sdk.business.search.entity

object SearchConstant{
    const val KEY_SEARCH = "search_key"
    const val KEY_SEARCH_TYPE = "search_tab"
}

enum class SearchTab(val type:Int) {
    ALL(100),SONG(0), ARTIST(2), ALBUM(1), PLAYLIST(3)
}

