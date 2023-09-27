package com.tencent.joox.sdk.data.net

class JOOXRequestCommonParam {
    companion object {
        const val CLIENT_TAG = "client_id="
        const val RESPONSE_TYPE = "response_type="
        val SCOPE = "scope=public"
        val bundle_id = "bundleid="        //1：Android 2:ios
        val country = "country="              //国家
        val language = "lang="                //语言
        val number = "num="                 //一页的数目
        val index = "index="                 //分页Index
    }
}