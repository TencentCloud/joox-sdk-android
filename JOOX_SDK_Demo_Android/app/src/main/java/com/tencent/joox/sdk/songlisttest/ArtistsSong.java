package com.tencent.joox.sdk.songlisttest;

import com.tencent.joox.sdk.TrackItem;

import java.util.List;

public class ArtistsSong {


    /**
     * artist_id : oW05+PCrMl3hZAgVFPMfqA==
     * tracks : {"items":[{"album_id":"m_lSQRnE8rDwvJxp+0nxKQ==","album_name":"陳慧嫻3In1珍藏集","artist_list":[{"id":"hVqlzeFcuyg7Tiyrr77ncA==","name":"陳慧嫻"},{"id":"oW05+PCrMl3hZAgVFPMfqA==","name":"張學友"}],"genre":"Pop","has_hifi":0,"has_hq":0,"id":"rBtoU+7znmlPGQAeMIvXDw==","images":[{"height":640,"url":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003PEJ1E30TI4S.jpg","width":640},{"height":300,"url":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003PEJ1E30TI4S.jpg","width":300},{"height":100,"url":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003PEJ1E30TI4S.jpg","width":100}],"language":"Cantonese","lrc_exist":1,"name":"夜半輕私語vs張學友","play_duration":212,"qrc_exist":0,"vip_flag":0}],"list_count":1,"next_index":20,"total_count":1000}
     */

    private String artist_id;
    private TracksBean tracks;

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public TracksBean getTracks() {
        return tracks;
    }

    public void setTracks(TracksBean tracks) {
        this.tracks = tracks;
    }

    public static class TracksBean {
        /**
         * items : [{"album_id":"m_lSQRnE8rDwvJxp+0nxKQ==","album_name":"陳慧嫻3In1珍藏集","artist_list":[{"id":"hVqlzeFcuyg7Tiyrr77ncA==","name":"陳慧嫻"},{"id":"oW05+PCrMl3hZAgVFPMfqA==","name":"張學友"}],"genre":"Pop","has_hifi":0,"has_hq":0,"id":"rBtoU+7znmlPGQAeMIvXDw==","images":[{"height":640,"url":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003PEJ1E30TI4S.jpg","width":640},{"height":300,"url":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003PEJ1E30TI4S.jpg","width":300},{"height":100,"url":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003PEJ1E30TI4S.jpg","width":100}],"language":"Cantonese","lrc_exist":1,"name":"夜半輕私語vs張學友","play_duration":212,"qrc_exist":0,"vip_flag":0}]
         * list_count : 1
         * next_index : 20
         * total_count : 1000
         */

        private int list_count;
        private int next_index;
        private int total_count;
        private List<TrackItem> items;

        public int getList_count() {
            return list_count;
        }

        public void setList_count(int list_count) {
            this.list_count = list_count;
        }

        public int getNext_index() {
            return next_index;
        }

        public void setNext_index(int next_index) {
            this.next_index = next_index;
        }

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public List<TrackItem> getItems() {
            return items;
        }

        public void setItems(List<TrackItem> items) {
            this.items = items;
        }


    }
}
