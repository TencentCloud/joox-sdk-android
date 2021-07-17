package com.tencent.joox.sdk;

import android.widget.Toast;

import com.joox.sdklibrary.PlayErrState;
import com.joox.sdklibrary.SDKInstance;
import com.joox.sdklibrary.SDKListener;
import com.joox.sdklibrary.kernel.dataModel.BaseAdInfo;
import com.joox.sdklibrary.kernel.dataModel.BaseSongInfo;
import com.joox.sdklibrary.player2.PlayCallBack;
import com.joox.sdklibrary.player2.PlayerState;
import com.tencent.mars.xlog.Log;

import java.util.ArrayList;
import java.util.List;

public class AppPlayManager implements PlayCallBack, SDKListener{
    private String TAG = "AppPlayManager";
    private List<TrackItem> items;
    private int index;
    private int playerState = PlayerState.IDLE;


    private List<PlayerListener> playerListeners = new ArrayList<>();

    private static AppPlayManager appPlayManager = new AppPlayManager();
    private int quality = BaseSongInfo.TYPE_96K;

    public AppPlayManager() {
        SDKInstance.getmInstance().registerListener(this);
    }

    public static AppPlayManager getInstance() {
        return appPlayManager;
    }

    public void setPlayList(List<TrackItem> items) {
        if(items == null){
            return;
        }
        this.items = new ArrayList<>(items);
        for (PlayerListener playerListener : playerListeners) {
            playerListener.onSongListChange();
        }
    }

    public void playIndex(int index) {
        if (items != null && !items.isEmpty()) {
            this.index = index;
            BaseSongInfo itemsBean = convert(items.get(index));
            playSong(itemsBean);

            for (PlayerListener playerListener : playerListeners) {
                playerListener.onPlaySongChange();
            }
        }
    }

    public void pre() {
        if (items != null && !items.isEmpty()) {
            if (index > 0) {
                index--;
            }
            BaseSongInfo itemsBean = convert(items.get(index));
            playSong(itemsBean);
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onPlaySongChange();
            }
        }
    }

    private void addIndex() {
        index ++;
        if (index >= items.size()) {
            index = index % items.size();
        }
    }

    public void next() {
        if (items != null && !items.isEmpty()) {
            addIndex();
            BaseSongInfo itemsBean = convert(items.get(index));
            playSong(itemsBean);
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onPlaySongChange();
            }
        }else{
            Toast.makeText(SDKApplication.Companion.getInstance(),"no more songs to play!",Toast.LENGTH_SHORT).show();
            SDKInstance.getmInstance().stopPlay();
        }
    }

    public void playSong(BaseSongInfo itemsBean) {
        SDKInstance.getmInstance().play(itemsBean, this);
        for (PlayerListener playerListener : playerListeners) {
            playerListener.onLoading(true);
        }
    }

    public void playAd(BaseAdInfo adInfo){
        SDKInstance.getmInstance().playAd(adInfo,this);
        for (PlayerListener playerListener : playerListeners) {
            playerListener.onLoading(true);
        }
    }

    public void play() {
        if (items != null) {
            playIndex(index);
        }
    }

    private BaseSongInfo convert(TrackItem itemsBean) {
        boolean isDown = SDKInstance.getmInstance().isDownFileExist(itemsBean.getId(), quality);
        int type;
        if (isDown) {
            Toast.makeText(SDKInstance.getmInstance().getmContext(),"play local file for this downloaded song!",Toast.LENGTH_SHORT).show();
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
        if(items.size() == 0){
            Log.e(TAG,"warning -> no current track Item!!!!");
            return new TrackItem();
        }
        return items.get(index);
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
        if (code == PlayErrState.SUCCESS) {
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onLoading(false);
            }
        } else if (code == PlayErrState.LIMIT_ERROR) {
            Toast.makeText(SDKApplication.Companion.getInstance(), "666限制， 播放失败", Toast.LENGTH_SHORT).show();
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onLoading(false);
            }
        } else if (code == PlayErrState.LOGIN_EXPIRED) {
            Toast.makeText(SDKApplication.Companion.getInstance(), "用户离线时间过长，无法收听歌曲，请登录", Toast.LENGTH_SHORT).show();
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onLoading(false);
            }
        } else {
            Toast.makeText(SDKApplication.Companion.getInstance(), "播放失败", Toast.LENGTH_SHORT).show();
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onLoading(false);
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
        return items;
    }

    public void stop() {
        SDKInstance.getmInstance().stopPlay();
        items.clear();
        index = 0;
        for (PlayerListener playerListener : playerListeners) {
            playerListener.onSongListChange();
        }
    }

    public void resumePlay() {
        if (items != null && !items.isEmpty()) {
            SDKInstance.getmInstance().resumePlay();
        }
    }

    public void pausePlay() {
        if (items != null && !items.isEmpty()) {
            SDKInstance.getmInstance().pausePlay();
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

        void onError(int code);

        void onLoading(boolean isLoading);

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
                playerListener.onLoading(nowLoading);
            }
        }

        if (playState == PlayerState.FINISH) {
            next();
        } else if (playState == PlayerState.ERROR) {
            Toast.makeText(SDKApplication.Companion.getInstance(), "播放错误", Toast.LENGTH_SHORT)
                    .show();
        } else if (playState == PlayerState.IDLE){
            for(PlayerListener playerListener : playerListeners){
                playerListener.onIdle();
            }
        }
    }
}
