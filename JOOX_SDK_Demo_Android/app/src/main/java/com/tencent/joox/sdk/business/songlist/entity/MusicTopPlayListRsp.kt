package com.tencent.joox.sdk.business.songlist.entity

import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.data.TrackItem
import com.tencent.joox.sdk.data.entity.CoverEntity
import com.tencent.joox.sdk.data.entity.TracksRspEntity

class MusicTopPlayListRsp {

    @SerializedName("data")
    lateinit var data: MusicTopPlayList

    @SerializedName("error_code")
    var errorCode: Int = 0

    @SerializedName("error")
    var error: String? = null


}

class MusicTopPlayList {

    @SerializedName("id")
    var id: String = ""

    @SerializedName("title")
    var name: String? = null

    @SerializedName("description")
    var des: String? = null

    @SerializedName("images")
    var coverList: ArrayList<CoverEntity>? = null

    @SerializedName("song_list")
    var tracks: ArrayList<TrackItem>? = null

}