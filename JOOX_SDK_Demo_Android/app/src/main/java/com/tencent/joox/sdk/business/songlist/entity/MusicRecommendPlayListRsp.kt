package com.tencent.joox.sdk.business.songlist.entity

import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.data.entity.CoverEntity
import com.tencent.joox.sdk.data.entity.TracksRspEntity

class MusicRecommendPlayListRsp {

    @SerializedName("id")
    var id: String = ""

    @SerializedName("name")
    var name: String? = null

    @SerializedName("images")
    var coverList: ArrayList<CoverEntity>? = null

    @SerializedName("tracks")
    var tracks: TracksRspEntity? = null

}