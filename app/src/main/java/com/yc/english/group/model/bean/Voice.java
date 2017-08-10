package com.yc.english.group.model.bean;

import java.io.File;

/**
 * Created by wanglin  on 2017/8/8 11:04.
 */

public class Voice {
    private String uri;
    private File file;
    private String duration;

    public Voice() {
    }

    public Voice(String uri, String duration) {
        this.uri = uri;
        this.duration = duration;
    }

    public Voice(File file, String duration) {
        this.file = file;
        this.duration = duration;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
