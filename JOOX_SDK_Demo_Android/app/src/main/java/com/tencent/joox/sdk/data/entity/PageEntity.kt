package com.tencent.joox.sdk.data.entity

open class PageEntity<T>(
    val state: PageState = PageState.IDEL,
    val rsp: T? = null,
    var pageIndex: PageIndex = PageIndex()
) {
}

enum class PageState {
    IDEL, EMPTY, SUCCESS, ERROR
}

class PageIndex(val nextIndex: Int = 0, val pageSize: Int = 0, val totalSize: Int = 0, val hasMore: Boolean = false) {

}