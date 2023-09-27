package com.tencent.joox.sdk.business.search.entity

import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.business.songlist.entity.AlbumEntity
import com.tencent.joox.sdk.data.TrackItem
import com.tencent.joox.sdk.data.entity.ArtistEntity
import com.tencent.joox.sdk.data.entity.PageEntity
import com.tencent.joox.sdk.data.entity.PageState

enum class SearchSection(val type:Int) {
    SONG(0), ALBUM(1), ARTIST(2), PLAY_LIST(3), BEST_MATCH(5)
}

class SearchAllRsp {
    @SerializedName("section_list")
    val sectionList: ArrayList<SearchSectionListEntity>? = null
}

class SearchAllPageEntity(state: PageState, rsp: ArrayList<SearchSectionListEntity>?) :
    PageEntity<ArrayList<SearchSectionListEntity>>(state, rsp) {
}

class SearchSectionListEntity {
    @SerializedName("section_type")
    val sectionType = 0

    @SerializedName("section_title")
    val sectionTitle: String = ""

    @SerializedName("item_list")
    val itemList: ArrayList<SearchItemListEntity>? = null
}


class SearchItemListEntity {
    val type: Int = 0

    @SerializedName("song")
    val song: ArrayList<SearchAllSongItem>? = null

    @SerializedName("singer")
    val artist: ArtistEntity? = null

    val album: AlbumEntity? = null

    @SerializedName("editor_playlist")
    val playList: SearchPlayListEntity? = null

}

class SearchAllSongItem {
    @SerializedName("song_info")
    val song: TrackItem? = null
    val lyric: String = ""
}