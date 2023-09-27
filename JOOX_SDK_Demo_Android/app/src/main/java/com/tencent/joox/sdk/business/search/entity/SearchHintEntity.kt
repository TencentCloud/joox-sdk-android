package com.tencent.joox.sdk.business.search.entity

import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.business.songlist.entity.AlbumEntity
import com.tencent.joox.sdk.data.TrackItem
import com.tencent.joox.sdk.data.entity.ArtistEntity

class SearchHintRsp {
    @SerializedName("item_list")
    val items: ArrayList<SearchHintItem>? = null

    @SerializedName("error_code")
    val errorCode: Int = 0
    val error: String = ""

}

class SearchHintItem {
    val type: Int = 0
    val album: AlbumEntity? = null
    @SerializedName("editor_playlist")
    val editorPlaylist: SearchPlayListEntity? = null
    @SerializedName("user_playlist")
    val userPlaylist: SearchPlayListEntity? = null
    val singer: ArtistEntity? = null
    @SerializedName("prompt_info")
    val promptInfo: SearchPromptItem? = null
    val song: ArrayList<SearchHintSongItem>? = null
}

class SearchHintSongItem {
    @SerializedName("song_info")
    val songInfo: TrackItem? = null
    val lyric: String = ""
}

class SearchPromptItem {
    val name: String = ""
}