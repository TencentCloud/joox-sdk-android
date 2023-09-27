package com.tencent.joox.sdk.business.test;

import java.util.List;

public class Artists {


    /**
     * artists : {"items":[{"id":"oW05+PCrMl3hZAgVFPMfqA==","images":[{"height":1000,"url":"https://test.imgcache.joox.com/preproduct_music/photo_hk/mid_singer_1000/b/6/ffb0eb46eaefbe8631996f8b1a9c49b6.jpg","width":1000},{"height":640,"url":"https://test.imgcache.joox.com/preproduct_music/photo_hk/mid_singer_640/b/6/ffb0eb46eaefbe8631996f8b1a9c49b6.jpg","width":640},{"height":300,"url":"https://test.imgcache.joox.com/preproduct_music/photo_hk/mid_singer_300/b/6/ffb0eb46eaefbe8631996f8b1a9c49b6.jpg","width":300},{"height":100,"url":"https://test.imgcache.joox.com/preproduct_music/photo_hk/mid_singer_100/b/6/ffb0eb46eaefbe8631996f8b1a9c49b6.jpg","width":100}],"name":"張學友"}]}
     */

    private ArtistsBean artists;

    public ArtistsBean getArtists() {
        return artists;
    }

    public void setArtists(ArtistsBean artists) {
        this.artists = artists;
    }

    public static class ArtistsBean {
        private List<ItemsBean> items;

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * id : oW05+PCrMl3hZAgVFPMfqA==
             * images : [{"height":1000,"url":"https://test.imgcache.joox.com/preproduct_music/photo_hk/mid_singer_1000/b/6/ffb0eb46eaefbe8631996f8b1a9c49b6.jpg","width":1000},{"height":640,"url":"https://test.imgcache.joox.com/preproduct_music/photo_hk/mid_singer_640/b/6/ffb0eb46eaefbe8631996f8b1a9c49b6.jpg","width":640},{"height":300,"url":"https://test.imgcache.joox.com/preproduct_music/photo_hk/mid_singer_300/b/6/ffb0eb46eaefbe8631996f8b1a9c49b6.jpg","width":300},{"height":100,"url":"https://test.imgcache.joox.com/preproduct_music/photo_hk/mid_singer_100/b/6/ffb0eb46eaefbe8631996f8b1a9c49b6.jpg","width":100}]
             * name : 張學友
             */

            private String id;
            private String name;
            private List<ImagesBean> images;

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

            public List<ImagesBean> getImages() {
                return images;
            }

            public void setImages(List<ImagesBean> images) {
                this.images = images;
            }

            public static class ImagesBean {
                /**
                 * height : 1000
                 * url : https://test.imgcache.joox.com/preproduct_music/photo_hk/mid_singer_1000/b/6/ffb0eb46eaefbe8631996f8b1a9c49b6.jpg
                 * width : 1000
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
    }
}
