package com.tencent.joox.sdk.business.subscribe.entity

import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.data.TrackItem
import com.tencent.joox.sdk.data.entity.CoverEntity
import com.tencent.joox.sdk.data.entity.TracksRspEntity

class SubscribeActionEntity {
    @SerializedName("subscribe_status")
    val subscribeStatus: Int = 0
}

class SubscribeSongEntity {
    val tracks: TracksRspEntity? = null
    val updateTime: Long = 0
}


class SubscribePlayListEntity {
    @SerializedName("subscribed_song_list")
    val playList: ArrayList<SubscribePlayList>? = null

    @SerializedName("list_count")
    val listCount = 0

    @SerializedName("error_code")
    val errorCode = 0

    val error = ""

    class SubscribePlayList {

        @SerializedName("images")
        val cover: ArrayList<CoverEntity>? = null

        @SerializedName("is_subscribed")
        val isSubscribed = 0

        @SerializedName("is_public")
        val isPublic = 0

        @SerializedName("list_type")
        val listType = 0

        @SerializedName("track_count")
        val trackCount = 0

        @SerializedName("id")
        val id = ""

        @SerializedName("sub_id")
        val subId = ""

        @SerializedName("name")
        val name = ""
    }
}