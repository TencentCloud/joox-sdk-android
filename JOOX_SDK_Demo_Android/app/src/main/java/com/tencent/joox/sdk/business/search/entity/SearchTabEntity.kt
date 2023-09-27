package com.tencent.joox.sdk.business.search.entity

import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.business.songlist.entity.AlbumEntity
import com.tencent.joox.sdk.data.TrackItem
import com.tencent.joox.sdk.data.entity.ArtistEntity
import com.tencent.joox.sdk.data.entity.PageEntity
import com.tencent.joox.sdk.data.entity.PageIndex
import com.tencent.joox.sdk.data.entity.PageState

class SearchTabPageEntity(state: PageState, rsp: ArrayList<SearchTabItem>?, pageIndex: PageIndex) :
    PageEntity<ArrayList<SearchTabItem>>(state, rsp, pageIndex) {
}

class SearchTabItem(var type:SearchTab, val item: Any) {
}

class SearchTabRsp {
    @SerializedName("next_page")
    val nextPage = 0

    @SerializedName("has_more")
    val hasMore = false

    @SerializedName("artists")
    val artists: ArrayList<ArtistEntity>? = null

    @SerializedName("tracks")
    val tracks: ArrayList<ArrayList<TrackItem>>? = null

    @SerializedName("albums")
    val albums: ArrayList<AlbumEntity>? = null

    @SerializedName("playlists")
    val playlists: ArrayList<SearchPlayListEntity>? = null
}


