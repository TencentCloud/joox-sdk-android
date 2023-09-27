package com.tencent.joox.sdk.business.player;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.joox.sdklibrary.PlayErrState;
import com.joox.sdklibrary.SDKInstance;
import com.joox.sdklibrary.SDKListener;
import com.joox.sdklibrary.kernel.auth.UserManager;
import com.joox.sdklibrary.kernel.dataModel.BaseSongInfo;
import com.joox.sdklibrary.kernel.dataModel.UserInfo;
import com.joox.sdklibrary.player2.PlayCallBack;
import com.joox.sdklibrary.player2.PlayerState;
import com.tencent.joox.sdk.SDKApplication;
import com.tencent.joox.sdk.data.TrackItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppPlayManager implements PlayCallBack, SDKListener{
    private String TAG = "AppPlayManager";
    private int index;

    //实际播放歌曲列表，因播放模式变化更新
    private List<TrackItem> playingSongList;
    //原始歌曲列表
    private List<TrackItem> originalSongList;
    private int playerState = PlayerState.IDLE;
    private PlayMode playMode = PlayMode.SHUFFLE;

    private List<PlayerListener> playerListeners = new ArrayList<>();

    private static AppPlayManager appPlayManager = new AppPlayManager();
    private int quality = BaseSongInfo.TYPE_96K;

    public AppPlayManager() {
        SDKInstance.getIns().registerListener(this);
        UserManager userManager = SDKInstance.getIns().getUserManager();
        if (userManager != null){
           UserInfo userInfo =  userManager.getUserInfo();
           if (userInfo != null && !userInfo.isVip()){
               playMode = PlayMode.SHUFFLE;
           }
        }
    }

    public static AppPlayManager getInstance() {
        return appPlayManager;
    }



    public void setPlayMode(PlayMode playModel) {
        if (this.playMode != playModel){
          rebuildPlayList(playModel);
        }
        this.playMode = playModel;
    }

    public PlayMode getPlayMode() {
        return playMode;
    }

    public void playMusicList(List<TrackItem> items, int curSongIndex, boolean showPlayPage){
        setPlayList(items);
        TrackItem curTrackItem = items.get(curSongIndex);
        int focusIndex = playingSongList.indexOf(curTrackItem);
        playIndex(focusIndex);
        if (showPlayPage){
            Context ctx = SDKApplication.Companion.getInstance().getApplicationContext();
            PlayerActivity.Companion.startPlay(ctx);
        }
    }

    private void setPlayList(List<TrackItem> items) {
        if(items == null){
            return;
        }
        this.originalSongList = new ArrayList<>(items);
        rebuildPlayList(playMode);
        for (PlayerListener playerListener : playerListeners) {
            playerListener.onSongListChange();
        }
    }

    private void rebuildPlayList(PlayMode playMode){
        TrackItem curTrackItem = null;
        if (playingSongList != null){
            curTrackItem = playingSongList.get(index);
            playingSongList.clear();
        }else {
            playingSongList = new ArrayList<>();
        }
        playingSongList.addAll(originalSongList);
        if (playMode == PlayMode.SHUFFLE){
            Collections.shuffle(playingSongList);
        }
        if (curTrackItem != null){
            index = playingSongList.indexOf(curTrackItem);
        }

    }


    private void playIndex(int index) {
        if (playingSongList != null && !playingSongList.isEmpty()) {
            this.index = index;
            BaseSongInfo itemsBean = convert(playingSongList.get(index));
            playSong(itemsBean);

            for (PlayerListener playerListener : playerListeners) {
                playerListener.onPlaySongChange();
            }
        }
    }

    public void pre() {
        if (playingSongList != null && !playingSongList.isEmpty()) {
            if (index > 0) {
                index--;
            }
            BaseSongInfo itemsBean = convert(playingSongList.get(index));
            playSong(itemsBean);
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onPlaySongChange();
            }
        }
    }

    private void addIndex() {
        index ++;
        if (index >= playingSongList.size()) {
            index = index % playingSongList.size();
        }
    }

    public void next() {
        if (playingSongList != null && !playingSongList.isEmpty()) {
            addIndex();
            BaseSongInfo itemsBean = convert(playingSongList.get(index));
            playSong(itemsBean);
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onPlaySongChange();
            }
        }else{
            Toast.makeText(SDKApplication.Companion.getInstance(),"no more songs to play!",Toast.LENGTH_SHORT).show();
            SDKInstance.getIns().stopPlay();
        }
    }

    private void playSong(BaseSongInfo itemsBean) {
        SDKInstance.getIns().play(itemsBean, this);
        for (PlayerListener playerListener : playerListeners) {
            playerListener.onPlayLoading(true);
        }
    }

    public void play() {
        if (playingSongList != null) {
            playIndex(index);
        }
    }

    private BaseSongInfo convert(TrackItem itemsBean) {
        boolean isDown = SDKInstance.getIns().isDownFileExist(itemsBean.getId(), quality);
        int type;
        if (isDown) {
            Toast.makeText(SDKInstance.getIns().getContext(),"play local file for this downloaded song!",Toast.LENGTH_SHORT).show();
            type = BaseSongInfo.TYPE_DOWN;
        } else {
            type = BaseSongInfo.TYPE_ONLINE;
        }
        BaseSongInfo baseSongInfo = new BaseSongInfo(type, itemsBean.getId());
        baseSongInfo.setDuration(itemsBean.getPlay_duration());
        baseSongInfo.setLabel(itemsBean.getTrack_label_flag());
        baseSongInfo.setAlbumId(itemsBean.getAlbum_id());
        baseSongInfo.setQuality(quality);
        return baseSongInfo;
    }

    public void setQuality(int quality) {
        this.quality = quality;
        play();
    }

    public TrackItem getCurrentTrackItem() {
        if(playingSongList.size() == 0){
            Log.e(TAG,"warning -> no current track Item!!!!");
            return new TrackItem();
        }
        return playingSongList.get(index);
    }

    public boolean isPlaying() {
        return playerState == PlayerState.PLAYING || playerState == PlayerState.LOADING;
    }

    public boolean isPrepared() {
        return playerState != PlayerState.IDLE&& playerState != PlayerState.ERROR && playerState != PlayerState.FINISH;
    }

    public boolean isPreparing() {
        return playerState == PlayerState.PREPARING;
    }

    public boolean isLoading() {
        return playerState == PlayerState.LOADING || playerState == PlayerState.PREPARING;
    }

    @Override
    public void onPlayResult(int code) {
        if (code != PlayErrState.SUCCESS){
            Toast.makeText(SDKApplication.Companion.getInstance(), "播放失败, 错误码:" + code, Toast.LENGTH_SHORT).show();
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onPlayError(code);
            }
        }else {
            BaseSongInfo curPlaySong = SDKInstance.getIns().getSongInfo();
            if (curPlaySong != null){
                Log.d(TAG, "play success:" + curPlaySong.getSongOpenId());
            }
        }
    }

    @Override
    public void onPlayProgress(long progress, long during) {
        for (PlayerListener playerListener : playerListeners) {
            playerListener.onPlayProgress(progress, during);
        }
    }

    public void addPlayerListeners(PlayerListener playerListener) {
        playerListeners.add(playerListener);
    }

    public void removePlayerListeners(PlayerListener playerListener) {
        playerListeners.remove(playerListener);
    }

    public List<TrackItem> getItems() {
        return originalSongList;
    }


    public void resumePlay() {
        if (SDKInstance.getIns().getSongInfo() != null) {
            SDKInstance.getIns().resumePlay();
        }
    }

    public void pausePlay() {
        if (SDKInstance.getIns().getSongInfo() != null) {
            SDKInstance.getIns().pausePlay();
        }
    }

    public void unInit(){
        try{
            for(PlayerListener playerListener : playerListeners){
                playerListeners.remove(playerListener);
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG,"AppPlayManager unInit error message is "+e.getMessage());
        }

    }

    public int getQuality() {
        return quality;
    }

    public interface PlayerListener {
        void onPlayProgress(long progress, long during);

        void onPlayError(int code);

        void onPlayStart();

        void onPlayPause();

        void onPlayStop();

        void onPlayLoading(boolean isLoading);

        void onSongListChange();

        void onPlaySongChange();

        void onIdle();
    }

    @Override
    public void currentAuthState(int authState) {
    }

    @Override
    public void updateCurrentPlayState(int playState) {
        boolean preLoading = isLoading();
        playerState = playState;
        boolean nowLoading = isLoading();

        if (preLoading != nowLoading) {
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onPlayLoading(nowLoading);
            }
        }

        if (playState == PlayerState.FINISH) {
            if (playMode == PlayMode.SINGLE){
                play();
            }else {
                next();
            }
        } else if (playerState == PlayerState.PLAYING){
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onPlayStart();
            }
        }else if (playState == PlayerState.ERROR) {
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onPlayError(0);
            }
            Toast.makeText(SDKApplication.Companion.getInstance(), "播放错误", Toast.LENGTH_SHORT)
                    .show();
        } else if (playState == PlayerState.IDLE){
            for(PlayerListener playerListener : playerListeners){
                playerListener.onIdle();
            }
        }
    }
}
