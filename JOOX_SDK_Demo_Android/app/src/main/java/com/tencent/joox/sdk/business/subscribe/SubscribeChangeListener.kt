package com.tencent.joox.sdk.business.subscribe

enum class LikeContentType{
    SONG, PLAY_LIST
}

interface SubscribeChangeListener{

    fun onSubscribeChanged(isLike:Boolean, contentType: LikeContentType, id:String)
}