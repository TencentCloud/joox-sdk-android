package com.tencent.joox.sdk.data.entity

import com.google.gson.annotations.SerializedName

class DiscoverItemEntity {

    @SerializedName("id")
    val id:String = ""

    @SerializedName("name")
    val name:String = ""

    @SerializedName("images")
    val coverUrl:ArrayList<CoverEntity> ? = null

    @SerializedName("song_list")
    val songList:ArrayList<CoverEntity> ? = null
}