package com.tencent.joox.sdk.data.entity

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.utils.gson.RawStringJsonAdapter

class ArtistEntity {

    @SerializedName("id")
    val id :String = ""

    @SerializedName("name")
    val name :String = ""

    @SerializedName("images")
    val coverList :ArrayList<CoverEntity>? = null

}