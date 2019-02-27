package com.yc.junior.english.base.model;

/**
 * Created by zhangkai on 2017/8/21.
 */

public class ShareInfo {

    private INFO info;

    public INFO getInfo() {
        return info;
    }

    public void setInfo(INFO info) {
        this.info = info;
    }

    public class INFO {
        private int type = 0;
        private String title;
        private String desp;

        public String getDesp() {
            return desp;
        }

        public void setDesp(String desp) {
            this.desp = desp;
        }

        private String img;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
