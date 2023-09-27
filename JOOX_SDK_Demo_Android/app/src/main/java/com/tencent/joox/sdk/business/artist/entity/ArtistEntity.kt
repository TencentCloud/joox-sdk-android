package com.tencent.joox.sdk.business.artist.entity

import com.google.gson.annotations.SerializedName
import com.tencent.joox.sdk.business.songlist.entity.AlbumEntity
import com.tencent.joox.sdk.data.TrackItem
import com.tencent.joox.sdk.data.entity.CoverEntity
import com.tencent.joox.sdk.data.entity.PageEntity
import com.tencent.joox.sdk.data.entity.PageIndex
import com.tencent.joox.sdk.data.entity.PageState
import com.tencent.joox.sdk.data.entity.TracksRspEntity


object ArtistHomeConstant {
    const val KEY_ARTIST_ID = "artist_id"
}

class ArtistHomePage(state: PageState, rsp: ArtistInfo?) :
    PageEntity<ArtistInfo>(state, rsp) {
}

class ArtistSongTabPage(state: PageState, rsp: ArtistSongTabData?, index: PageIndex) :
    PageEntity<ArtistSongTabData>(state, rsp, index) {
}

class ArtistSongTabData {
    @SerializedName("artist_id")
    val artistId : String =""
    val tracks: TracksRspEntity? = null
}

class ArtistAlbumTabPage(state: PageState, rsp: ArtistAlbumTabData?, index: PageIndex) :
    PageEntity<ArtistAlbumTabData>(state, rsp, index) {
}

class ArtistAlbumTabData {
    @SerializedName("artist_id")
    val artistId : String =""
    val albums: AlbumRspEntity? = null
}

class AlbumRspEntity {

    @SerializedName("list_count")
    var listCount = 0

    @SerializedName("next_index")
    var nextIndex = 0

    @SerializedName("totalCount")
    var totalCount = 0

    var items: ArrayList<AlbumEntity>? = null

}


class ArtistInfo {

    val id: String = ""

    val name: String = ""

    @SerializedName("images")
    val cover: ArrayList<CoverEntity>? = null

    @SerializedName("song_num")
    val songNum: Int = 0

    @SerializedName("album_num")
    val albumNum: Int = 0
}