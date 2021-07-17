//package com.tencent.joox.sdk
//
//import com.tencent.sdklibrary.SDKInstance
//import JSONParser
//import StringUtil
//import BaseSongInfo
//import SceneBase
//import org.json.JSONException
//import org.json.JSONObject
//import java.util.ArrayList
//import java.util.HashMap
//
//class PlayDataManager {
//
//    fun getPlayData(){
//        var requestParamBuilder =  RequestParamBuilder()
//        SDKInstance.getmInstance().doJooxRequest("third_tag?",requestParamBuilder.setCommonParam().setCountryParam().setLanguageParam().getResultString(),object: SceneBase.OnSceneBack{
//            override fun onSuccess(responseCode: Int, JsonResult: String?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                val parser = JSONParser(JsonResult)
//                val errorCode = parser.getString("error_code")
//                if (StringUtil.isNullOrNil(errorCode)) {
//                    val tag_id = parser.getString("tag_id")
//                    getTagPlaylists(tag_id)
//                }
//            }
//
//            override fun onFail(errCode: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        })
//
//    }
//
//    fun getTagPlaylists(tagId:String) {
//        var playlistParamBuilder = RequestParamBuilder()
//        SDKInstance.getmInstance().doJooxRequest("v1/tag/" + tagId + "playlists?", playlistParamBuilder.setCountryParam().setLanguageParam().setNumParam().setIndexParam(0).getResultString(), object : SceneBase.OnSceneBack {
//            override fun onSuccess(responseCode: Int, JsonResult: String?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                val parser = JSONParser(JsonResult)
//                val errorCode = parser.getString("error_code")
//                if (StringUtil.isNullOrNil(errorCode)) {
//                    val playlist = parser.getJSONObj("playlists")
//                    val itemArray = playlist.getJSONArray("items")
//                    val item_array_size = itemArray.length()
//                    val albumInfoList = ArrayList<AlbumInfo>()
//                    for (i in 0 until item_array_size) {
//                        albumInfoList.add(transformJSONObjToPlaylists(itemArray.getJSONObject(i)))
//                    }
//                    getAlbumTrack(albumInfoList.get(0).albumId)
//                }
//            }
//
//            override fun onFail(errCode: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        })
//    }
//
//    fun getAlbumTrack(albumId:String){
//        var albumlistParamBuilder = RequestParamBuilder()
//        SDKInstance.getmInstance().doJooxRequest("v1/playlist/"+albumId+"/tracks?",albumlistParamBuilder.setCountryParam().setLanguageParam().setNumParam().setIndexParam(0).getResultString(),object:SceneBase.OnSceneBack{
//            override fun onSuccess(responseCode: Int, JsonResult: String?) {
//                var parser = JSONParser(JsonResult)
//                val errorCode = parser.getString("error_code")
//                if (StringUtil.isNullOrNil(errorCode.trim { it <= ' ' })) {
//                    val tracks = parser.getJSONObj("tracks")
//                    val songArray = tracks.getJSONArray("items")
//                    val size = songArray.length()
//                    val songList = ArrayList<BaseSongInfo>()
//                    for (i in 0 until size) {
//                        songList.add(transformJsonIntoSong(songArray.getJSONObject(i)))
//                    }
//                }
//            }
//
//            override fun onFail(errCode: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        })
//    }
//
//    fun getTrackInfo(songOpenId:String){
//        var trackParamBuilder = RequestParamBuilder()
//        SDKInstance.getmInstance().doJooxRequest("v1/track/"+songOpenId+"?",trackParamBuilder.setLanguageParam().setCountryParam().getResultString(),object:SceneBase.OnSceneBack{
//            override fun onSuccess(responseCode: Int, JsonResult: String?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                //todo get url of final track!
//            }
//
//            override fun onFail(errCode: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        })
//
//    }
//
//    fun transformJsonIntoSong(songObject:JSONObject): BaseSongInfo{
//        val info = BaseSongInfo(BaseSongInfo.TYPE_ONLINE)
//        info.setDuration(java.lang.Long.valueOf(songObject.getString("play_duration")))
//        info.setSongOpenId(songObject.getString("id"))
//        info.setAlbumName(songObject.getString("album_name"))
//        info.setName(songObject.getString("name"))
//        info.setAlbumId(songObject.getString("album_id"))
//        val singerArray = songObject.getJSONArray("artist_list")
//        val singer_size = singerArray.length()
//
//        val imgArray = songObject.getJSONArray("images")
//        val size = imgArray.length()
//        val imgHashMap = HashMap<Int, String>()
//        return info
//    }
//
//
//
//    @Throws(JSONException::class)
//    fun transformJSONObjToPlaylists(jsonItem: JSONObject): AlbumInfo {
//        val album = AlbumInfo()
//        album.setDescription(jsonItem.getString("description"))
//        album.setTrack_count(jsonItem.getInt("track_count"))
//        album.setId(jsonItem.getString("id"))
//        album.setAlbumName(jsonItem.getString("name"))
//        return album
//    }
//
//    }
//
