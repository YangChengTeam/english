package com.yc.english.group.model.bean;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/28 09:05.
 * 作业信息
 */

public class TaskItem {

    private String publishTime;
    //作业类型 0,纯文字 1.语音 2.图片 3.文档
    //可能返回一种类型，也可能返回多种类型
    private List<Integer> types;
    private String content;//作业内容 仅在返回作业类型为0的情况下有该字段

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public List<Integer> getTypes() {
        return types;
    }

    public void setTypes(List<Integer> types) {
        this.types = types;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
