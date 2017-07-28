package com.yc.english.group.model.bean;

/**
 * Created by wanglin  on 2017/7/28 16:03.
 * 作业完成群成员信息
 */

public class TaskFinishedInfo {
    private String name;
    private String imgUrl;
    private String finishedTime;

    public TaskFinishedInfo() {
    }

    public TaskFinishedInfo(String name, String imgUrl, String finishedTime) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.finishedTime = finishedTime;
    }

    public TaskFinishedInfo(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }
}
