package com.tencent.joox.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DownDB {

    private SharedPreferences sharedPreferences;

    private Gson gson  = new GsonBuilder().create();

    private Set<TrackItem> trackCache = new HashSet<TrackItem>();

    public DownDB(Context context) {
       //这个uid仅用于测试   可自行通过open api 拉去uid
        String uid = context.getSharedPreferences("SDK_FOR_THIRD_SP", Context.MODE_PRIVATE).getString("openId", null);
        //尽量使用db
        sharedPreferences = context.getSharedPreferences("down_song_" + uid, Context.MODE_PRIVATE);
        initTrackCache();
    }

    public void insertDownSong(TrackItem trackItem) {
        String value = sharedPreferences.getString("test_song", null);
        if(trackCache.contains(trackItem)){
            return;
        }
        trackCache.add(trackItem);
        if (!TextUtils.isEmpty(value)) {
            HashSet<TrackItem> trackItems = gson.fromJson(value, new TypeToken<HashSet<TrackItem>>(){}.getType());
            trackItems.add(trackItem);
            sharedPreferences.edit().putString("test_song", gson.toJson(trackItems)).apply();
        } else {
            Set<TrackItem> trackItems = new HashSet<>();
            trackItems.add(trackItem);
            sharedPreferences.edit().putString("test_song", gson.toJson(trackItems)).apply();
        }
    }

    public void deleteDownSong(TrackItem trackItem){
        trackCache.remove(trackItem);
        sharedPreferences.edit().putString("test_song",gson.toJson(trackCache)).apply();

    }

    public List<TrackItem> getAllDownSongs() {

        return new ArrayList<TrackItem>(trackCache);
    }

    private void initTrackCache(){
        String value = sharedPreferences.getString("test_song", null);
        if(!TextUtils.isEmpty(value)){
            List<TrackItem> trackItems = gson.fromJson(value, new TypeToken<List<TrackItem>>(){}.getType());
            trackCache.clear();
            trackCache.addAll(trackItems);
        }

    }
}
