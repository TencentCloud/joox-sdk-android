package com.tencent.joox.sdk.business.search.entity

import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.data.entity.CoverEntity

class SearchPlayListEntity {

    @SerializedName("id")
    val id:String = ""

    @SerializedName("name")
    val name:String = ""

    @SerializedName("images")
    val coverUrl:ArrayList<CoverEntity> ? = null

    @SerializedName("track_count")
    val trackCount:Int = 0

    @SerializedName("publish_date")
    val publishDate:String = ""
}