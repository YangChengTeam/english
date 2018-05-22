package com.yc.english.group.model.bean;

import java.io.File;

/**
 * Created by wanglin  on 2017/8/8 11:04.
 */

public class Voice {
    private String path;
    private File file;
    private String duration;

    public Voice() {
    }

    public Voice(String uri, String duration) {
        this.path = uri;
        this.duration = duration;
    }

    public Voice(File file, String duration) {
        this.file = file;
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
