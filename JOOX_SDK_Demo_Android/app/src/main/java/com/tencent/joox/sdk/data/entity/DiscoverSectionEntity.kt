package com.tencent.joox.sdk.data.entity

import com.google.gson.annotations.SerializedName


class DiscoverSectionEntity{

    @SerializedName("section_id")
    val sectionId:Int = 0


    @SerializedName("section_logic_id")
    val sectionLogicId:Int = 0

    @SerializedName("type")
    val type:Int = 0

    @SerializedName("title")
    val title:String =""

    @SerializedName("jump_url_v2")
    val jumpUrl:String =""

    @SerializedName("itemlist")
    val itemList:ArrayList<DiscoverItemEntity> = arrayListOf()
 }