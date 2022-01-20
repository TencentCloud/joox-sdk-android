package com.tencent.joox.sdk;

import java.util.ArrayList;
import java.util.HashMap;

import com.joox.sdklibrary.kernel.dataModel.BaseSongInfo;

public class AlbumInfo {

    private AlbumBaseInfo info = new AlbumBaseInfo();

    private String description;

    private int track_count;

    private HashMap<Integer,String> picDensity = new HashMap<Integer,String>();

    private ArrayList<BaseSongInfo> songList;

    public AlbumBaseInfo getInfo() {
        return info;
    }

    public void setInfo(AlbumBaseInfo info) {
        this.info = info;
    }

    public ArrayList<BaseSongInfo> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<BaseSongInfo> songList) {
        this.songList = songList;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTrack_count(int track_count) {
        this.track_count = track_count;
    }

    public int getTrack_count() {
        return track_count;
    }

    public void setId(String albumOpenId){
        info.setAlbumOpenId(albumOpenId);
    }

    public String getAlbumId(){
        return info.getAlbumOpenId();
    }
    public void setAlbumName(String name){
        info.setName(name);
    }

    public String getAlbumName(){
        return info.getName();
    }

    public void setAlbumImgUrl(HashMap<Integer,String> picUrlHashMap){
        picDensity = picUrlHashMap;
    }

    public String getAlbumImgUrl(int density){
        return picDensity.get(density);
    }

    public String getAlbumImgUrl(){
        return picDensity.get(300);
    }
}

