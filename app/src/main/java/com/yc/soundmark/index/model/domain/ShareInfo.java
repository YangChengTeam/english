package com.yc.soundmark.index.model.domain;

/**
 * Created by wanglin  on 2018/10/29 09:29.
 */
public class ShareInfo {
    private String title;
    private String content;
    private String url;
    private int share_add_hour;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getShare_add_hour() {
        return share_add_hour;
    }

    public void setShare_add_hour(int share_add_hour) {
        this.share_add_hour = share_add_hour;
    }
}
