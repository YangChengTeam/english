package com.yc.english.news.bean;

import java.io.Serializable;

/**
 * Created by wanglin  on 2017/9/6 09:56.
 */

public class NewsInfo implements Serializable {
    private String title;
    private String url;

    public NewsInfo() {
    }

    public NewsInfo(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
