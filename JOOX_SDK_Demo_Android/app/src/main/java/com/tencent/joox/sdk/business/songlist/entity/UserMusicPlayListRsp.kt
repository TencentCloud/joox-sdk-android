package com.tencent.joox.sdk.business.songlist.entity

import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.data.TrackItem
import com.tencent.joox.sdk.data.entity.CoverEntity
import com.tencent.joox.sdk.data.entity.TracksRspEntity

class UserMusicPlayListRsp {

    @SerializedName("id")
    var id: String = ""

    @SerializedName("sub_id")
    var subId: String = ""


    @SerializedName("list_type")
    var listType: Int = 0

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
    var tracks: ArrayList<TrackItem>? = null

}