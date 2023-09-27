package com.tencent.joox.sdk.business.songlist.entity

import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.data.entity.CoverEntity
import com.tencent.joox.sdk.data.entity.TracksRspEntity

class UserFavPlayListRsp {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("sub_id")
    var subId: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("images")
    var coverList: ArrayList<CoverEntity>? = null

    @SerializedName("publish_date")
    var publishDate: String? = null

    @SerializedName("total_count")
    var totalCount: Int = 0

    @SerializedName("next_index")
    var nextIndex: Int = 0

    @SerializedName("has_more")
    var hasMore: Boolean = false

    @SerializedName("tracks")
    var tracks: TracksRspEntity? = null

}