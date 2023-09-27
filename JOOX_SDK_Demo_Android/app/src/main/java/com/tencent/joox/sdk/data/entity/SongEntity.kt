package com.tencent.joox.sdk.data.entity

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.utils.gson.RawStringJsonAdapter

class SongEntity {

    @JsonAdapter(RawStringJsonAdapter::class)
    val id :String = ""

    val name :String = ""

    @SerializedName("artist_list")
    val artistList : ArrayList<ArtistEntity>? = null
}