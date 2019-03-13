package com.yc.english.vip.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin  on 2019/3/13 08:34.
 */
public class VipGoodInfo {
    private String title;
    @JSONField(name = "sub_title")
    private String content;

    private int id;
    private int sort;

    private int good_id;
    private int num;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getGood_id() {
        return good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
