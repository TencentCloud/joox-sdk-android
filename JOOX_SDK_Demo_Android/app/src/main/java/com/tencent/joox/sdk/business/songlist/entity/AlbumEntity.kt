package com.tencent.joox.sdk.business.songlist.entity

import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.data.entity.ArtistEntity
import com.tencent.joox.sdk.data.entity.CoverEntity
import com.tencent.joox.sdk.data.entity.TracksRspEntity

class AlbumEntity {

    @SerializedName("id")
    var id: String = ""

    @SerializedName("name")
    var name: String = ""

    @SerializedName("images")
    var coverList: ArrayList<CoverEntity>? = null

    @SerializedName("vip_flag")
    var vipFlag: Int = 0

    @SerializedName("artist_list")
    var artistList: ArrayList<ArtistEntity>? = null

    @SerializedName("tracks")
    var tracks: TracksRspEntity? = null

}