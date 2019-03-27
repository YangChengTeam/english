package com.yc.junior.english.composition.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by wanglin  on 2018/3/7 17:57.
 */

public class VersionInfo {
    private List<VersionDetailInfo> topic; // 话题选项
    @JSONField(name = "grade_id")
    private List<VersionDetailInfo> grade;// 年级
    private List<VersionDetailInfo> type;// 类型
    @JSONField(name = "ticai")
    private List<VersionDetailInfo> theme;// 体裁


    public List<VersionDetailInfo> getTopic() {
        return topic;
    }

    public void setTopic(List<VersionDetailInfo> topic) {
        this.topic = topic;
    }

    public List<VersionDetailInfo> getGrade() {
        return grade;
    }

    public void setGrade(List<VersionDetailInfo> grade) {
        this.grade = grade;
    }

    public List<VersionDetailInfo> getType() {
        return type;
    }

    public void setType(List<VersionDetailInfo> type) {
        this.type = type;
    }

    public List<VersionDetailInfo> getTheme() {
        return theme;
    }

    public void setTheme(List<VersionDetailInfo> theme) {
        this.theme = theme;
    }
}
