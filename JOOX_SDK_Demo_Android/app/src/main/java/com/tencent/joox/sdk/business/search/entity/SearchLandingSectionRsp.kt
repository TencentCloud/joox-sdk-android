package com.tencent.joox.sdk.business.search.entity

import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.data.entity.ArtistEntity

class SearchLandingSectionRsp {

    @SerializedName("hot_keyword_list")
    val hotWordEntityList: ArrayList<HotWordEntity>? = null

    @SerializedName("recommend_singer_list")
    val singerList: ArrayList<ArtistEntity>? = null

    @SerializedName("recommend_diss")
    val playList: ArrayList<SearchPlayListEntity>? = null


    fun isEmpty(): Boolean {
        return hotWordEntityList?.isEmpty() == true && singerList?.isEmpty() == true && playList?.isEmpty() == true
    }
}

class HotWordEntity {
    val word: String = ""
}