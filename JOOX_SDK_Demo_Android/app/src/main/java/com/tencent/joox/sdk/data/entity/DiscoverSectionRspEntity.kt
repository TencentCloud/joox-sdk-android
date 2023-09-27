package com.tencent.joox.sdk.data.entity

import com.google.gson.annotations.SerializedName

/**
 * @author: limuyang
 * @date: 2019-12-06
 * @Description:
 */
class DiscoverSectionRspEntity{

    @SerializedName("error_code")
    val errorCode:Int = 0

    @SerializedName("error")
    val error:String =""

    @SerializedName("data")
    val itemList:ArrayList<DiscoverSectionEntity> = arrayListOf()
 }