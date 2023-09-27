package com.tencent.joox.sdk.business.songlist.entity

import com.tencent.joox.sdk.data.entity.PageEntity
import com.tencent.joox.sdk.data.entity.PageIndex
import com.tencent.joox.sdk.data.entity.PageState

class MusicPlayListPage(state: PageState, rsp: MusicPlayListEntity?, pageIndex: PageIndex) :
    PageEntity<MusicPlayListEntity>(state, rsp, pageIndex) {
}