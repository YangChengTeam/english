package com.yc.junior.english.speak.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by admin on 2017/10/25.
 */

public class ListenEnglishBean implements Serializable {
    private String id;

    @JSONField(name = "type_id")
    private String typeId;

    private String title;

    @JSONField(name = "word_file")
    private String wordFile;

    private String mp3;

    private String img;

    @JSONField(name = "view_num")
    private String viewNum;

    private String flag;

    private String sort;

    @JSONField(name = "add_time")
    private String addTime;

    @JSONField(name = "add_date")
    private String addDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWordFile() {
        return wordFile;
    }

    public void setWordFile(String wordFile) {
        this.wordFile = wordFile;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getViewNum() {
        return viewNum;
    }

    public void setViewNum(String viewNum) {
        this.viewNum = viewNum;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }
}
