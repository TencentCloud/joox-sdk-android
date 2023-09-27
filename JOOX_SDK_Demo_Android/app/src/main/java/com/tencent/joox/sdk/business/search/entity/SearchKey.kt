package com.tencent.joox.sdk.business.search.entity

data class SearchKey(val word:String, val from:SearchFrom = SearchFrom.USER_INPUT)

enum class SearchFrom{
    HINT_PROMPT, USER_INPUT
}