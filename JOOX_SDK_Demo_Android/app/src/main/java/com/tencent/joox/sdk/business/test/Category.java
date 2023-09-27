package com.tencent.joox.sdk.business.test;

import java.io.Serializable;
import java.util.List;

public class Category {


    /**
     * categories : [{"category_name":"香港歌手","tag_count":3,"tag_list":[{"tag_id":"48","tag_name":"Male Artists - HK"},{"tag_id":"49","tag_name":"Female Artists - HK"},{"tag_id":"50","tag_name":"Bands and Groups - HK"}]},{"category_name":"韓國歌手","tag_count":3,"tag_list":[{"tag_id":"60","tag_name":"Male Artists - Korea"},{"tag_id":"61","tag_name":"Female Artists - Korea"},{"tag_id":"62","tag_name":"Bands and Groups - Korea"}]},{"category_name":"華語歌手","tag_count":3,"tag_list":[{"tag_id":"51","tag_name":"Male Artists - Asia"},{"tag_id":"52","tag_name":"Female Artists - Asia"},{"tag_id":"53","tag_name":"Bands and Groups - Asia"}]},{"category_name":"歐美歌手","tag_count":3,"tag_list":[{"tag_id":"54","tag_name":"Male Artists - International"},{"tag_id":"55","tag_name":"Female Artists - International"},{"tag_id":"56","tag_name":"Bands and Groups - International"}]},{"category_name":"日本歌手","tag_count":3,"tag_list":[{"tag_id":"57","tag_name":"Male Artists - Japan"},{"tag_id":"58","tag_name":"Female Artists - Japan"},{"tag_id":"59","tag_name":"Bands and Groups - Japan"}]}]
     * category_count : 5
     */

    private int category_count;
    private List<CategoriesBean> categories;

    public int getCategory_count() {
        return category_count;
    }

    public void setCategory_count(int category_count) {
        this.category_count = category_count;
    }

    public List<CategoriesBean> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoriesBean> categories) {
        this.categories = categories;
    }

    public static class CategoriesBean {
        /**
         * category_name : 香港歌手
         * tag_count : 3
         * tag_list : [{"tag_id":"48","tag_name":"Male Artists - HK"},{"tag_id":"49","tag_name":"Female Artists - HK"},{"tag_id":"50","tag_name":"Bands and Groups - HK"}]
         */

        private String category_name;
        private int tag_count;
        private List<TagListBean> tag_list;

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public int getTag_count() {
            return tag_count;
        }

        public void setTag_count(int tag_count) {
            this.tag_count = tag_count;
        }

        public List<TagListBean> getTag_list() {
            return tag_list;
        }

        public void setTag_list(List<TagListBean> tag_list) {
            this.tag_list = tag_list;
        }

        public static class TagListBean implements Serializable {
            /**
             * tag_id : 48
             * tag_name : Male Artists - HK
             */

            private String tag_id;
            private String tag_name;

            public String getTag_id() {
                return tag_id;
            }

            public void setTag_id(String tag_id) {
                this.tag_id = tag_id;
            }

            public String getTag_name() {
                return tag_name;
            }

            public void setTag_name(String tag_name) {
                this.tag_name = tag_name;
            }
        }
    }
}
