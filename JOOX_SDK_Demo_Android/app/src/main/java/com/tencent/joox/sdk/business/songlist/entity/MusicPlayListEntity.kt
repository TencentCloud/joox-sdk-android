package com.tencent.joox.sdk.business.songlist.entity

import com.joox.sdklibrary.SDKInstance
import com.tencent.joox.sdk.R
import com.tencent.joox.sdk.SDKApplication
import com.tencent.joox.sdk.business.subscribe.entity.SubscribeSongEntity
import com.tencent.joox.sdk.data.entity.ArtistEntity
import com.tencent.joox.sdk.data.entity.CoverEntity
import com.tencent.joox.sdk.data.entity.TracksRspEntity
import com.tencent.joox.sdk.utils.ui.ResourceUtil


enum class MusicPlayListType(val type: Int) {
    EDITOR_TOP_LIST(2),
    EDITOR_RECOMMEND_ALBUM(4),
    EDITOR_RECOMMEND_PLAYLIST(5),
    ML_RECOMMEND_PLAYLIST(6),
    ML_RECOMMEND_ALBUM(7),
    ML_MULTIPLE_SINGER_PLAYLIST(8),

    USER_SUBSCRIBE_PLAYLIST(100),
    USER_FAV_PLAYLIST(200)
}

class MusicPlayListEntity private constructor(
    val id: String? = null,
    val subId: String? = null,
    val listType: Int = 0,
    val name: String? = null,
    val coverList: ArrayList<CoverEntity>? = null,
    val vipFlag: Int = 0,
    val artistList: ArrayList<ArtistEntity>? = null,
    val tracks: TracksRspEntity? = null
) {

    private constructor(builder: Builder) : this(
        builder.id,
        builder.subId,
        builder.listType,
        builder.name,
        builder.coverList,
        builder.vipFlag,
        builder.artistList,
        builder.tracks
    )

    class Builder {
        var id: String = ""
            private set
        var subId: String = ""
            private set
        var listType: Int = 0
            private set
        var name: String = ""
            private set
        var vipFlag: Int = 0
            private set
        var coverList: ArrayList<CoverEntity>? = null
            private set
        var artistList: ArrayList<ArtistEntity>? = null
            private set
        var tracks: TracksRspEntity? = null
            private set

        fun setId(id: String) = apply {
            this.id = id
        }


        fun setSubId(subId: String) = apply {
            this.subId = subId
        }

        fun setName(name: String) = apply {
            this.name = name
        }

        fun setListType(listType: Int) = apply {
            this.listType = listType
        }

        fun setVipFlag(vipFlag: Int) = apply {
            this.vipFlag = vipFlag
        }

        fun setCoverList(coverList: ArrayList<CoverEntity>?) = apply {
            this.coverList = coverList
        }

        fun setArtistList(artistList: ArrayList<ArtistEntity>?) = apply {
            this.artistList = artistList
        }


        fun setTractList(tracks: TracksRspEntity?) = apply {
            this.tracks = tracks
        }

        fun build() = MusicPlayListEntity(this)
    }


    companion object {


        fun generateMusicPlayList(rsp: MusicTopPlayListRsp): MusicPlayListEntity {
            val trackEntity = TracksRspEntity()
            trackEntity.items = rsp.data.tracks
            return Builder()
                    .setId(rsp.data.id)
                    .setListType(2)
                    .setName(rsp.data.name ?: "")
                    .setCoverList(rsp.data.coverList)
                    .setTractList(trackEntity)
                    .build()
        }

        fun generateMusicPlayList(rsp: AlbumEntity): MusicPlayListEntity {
            return Builder()
                    .setId(rsp.id)
                    .setListType(1)
                    .setName(rsp.name ?: "")
                    .setCoverList(rsp.coverList)
                    .setArtistList(rsp.artistList)
                    .setTractList(rsp.tracks)
                    .build()
        }

        fun generateMusicPlayList(rsp: MusicRecommendPlayListRsp): MusicPlayListEntity {
            return Builder()
                    .setId(rsp.id)
                    .setListType(0)
                    .setName(rsp.name ?: "")
                    .setCoverList(rsp.coverList)
                    .setTractList(rsp.tracks)
                    .build()
        }

        fun generateUserMusicPlayList(rsp: UserMusicPlayListRsp): MusicPlayListEntity {
            val tracks = TracksRspEntity()
            tracks.list_count = rsp.tracks?.size ?: 0
            tracks.total_count = rsp.totalCount
            tracks.next_index = rsp.nextIndex
            tracks.items = rsp.tracks

            return Builder()
                    .setId(rsp.subId)
                    .setSubId(rsp.id)
                    .setListType(rsp.listType)
                    .setName(rsp.name ?: "")
                    .setCoverList(rsp.coverList)
                    .setTractList(tracks)
                    .build()

        }

        fun generateFavMusicPlayList(rsp: SubscribeSongEntity): MusicPlayListEntity {
            val coverUri = ResourceUtil.resIdToUrl(SDKApplication.instance.applicationContext, R.drawable.icon_user_fav)
            val cover = CoverEntity(0, 0, coverUri)
            val coverList = arrayListOf(cover)
            val userName = SDKInstance.getIns().userManager.userInfo.nickname
            return Builder()
                    .setName("${userName}'s favorite")
                    .setCoverList(coverList)
                    .setTractList(rsp.tracks)
                    .build()
        }
    }
}
