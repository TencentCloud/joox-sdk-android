package com.tencent.joox.sdk;

import java.util.List;

public class TrackItem {

        /**
         * album_id : m_lSQRnE8rDwvJxp+0nxKQ==
         * album_name : 陳慧嫻3In1珍藏集
         * artist_list : [{"id":"hVqlzeFcuyg7Tiyrr77ncA==","name":"陳慧嫻"},{"id":"oW05+PCrMl3hZAgVFPMfqA==","name":"張學友"}]
         * genre : Pop
         * has_hifi : 0
         * has_hq : 0
         * id : rBtoU+7znmlPGQAeMIvXDw==
         * images : [{"height":640,"url":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003PEJ1E30TI4S.jpg","width":640},{"height":300,"url":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003PEJ1E30TI4S.jpg","width":300},{"height":100,"url":"https://y.gtimg.cn/music/photo_new/T002R300x300M000003PEJ1E30TI4S.jpg","width":100}]
         * language : Cantonese
         * lrc_exist : 1
         * name : 夜半輕私語vs張學友
         * play_duration : 212
         * qrc_exist : 0
         * vip_flag : 0
         */

        private String album_id;
        private String album_name;
        private String genre;
        private int has_hifi;
        private int has_hq;
        private String id;
        private String language;
        private int lrc_exist;
        private String name;
        private int play_duration;
        private int qrc_exist;
        private int vip_flag;
        private List<ArtistListBean> artist_list;
        private List<ImagesBean> images;
        private int track_label_flag;

        public String getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(String album_id) {
            this.album_id = album_id;
        }

        public String getAlbum_name() {
            return album_name;
        }

        public void setAlbum_name(String album_name) {
            this.album_name = album_name;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public int getHas_hifi() {
            return has_hifi;
        }

        public void setHas_hifi(int has_hifi) {
            this.has_hifi = has_hifi;
        }

        public int getHas_hq() {
            return has_hq;
        }

        public void setHas_hq(int has_hq) {
            this.has_hq = has_hq;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public int getLrc_exist() {
            return lrc_exist;
        }

        public void setLrc_exist(int lrc_exist) {
            this.lrc_exist = lrc_exist;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPlay_duration() {
            return play_duration;
        }

        public void setPlay_duration(int play_duration) {
            this.play_duration = play_duration;
        }

        public int getQrc_exist() {
            return qrc_exist;
        }

        public void setQrc_exist(int qrc_exist) {
            this.qrc_exist = qrc_exist;
        }

        public int getVip_flag() {
            return vip_flag;
        }

        public void setVip_flag(int vip_flag) {
            this.vip_flag = vip_flag;
        }

        public List<ArtistListBean> getArtist_list() {
            return artist_list;
        }

        public void setArtist_list(List<ArtistListBean> artist_list) {
            this.artist_list = artist_list;
        }

        public List<ImagesBean> getImages() {
            return images;
        }

        public void setImages(List<ImagesBean> images) {
            this.images = images;
        }


    public int getTrack_label_flag() {
        return track_label_flag;
    }

    public void setTrack_label_flag(int track_label_flag) {
        this.track_label_flag = track_label_flag;
    }

    public static class ArtistListBean {
            /**
             * id : hVqlzeFcuyg7Tiyrr77ncA==
             * name : 陳慧嫻
             */

            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class ImagesBean {
            /**
             * height : 640
             * url : https://y.gtimg.cn/music/photo_new/T002R300x300M000003PEJ1E30TI4S.jpg
             * width : 640
             */

            private int height;
            private String url;
            private int width;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }
        }
}
