package com.yc.junior.english.news.bean;

import com.yc.junior.english.weixin.model.domain.CourseInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/9/7 10:58.
 */

public class CourseInfoWrapper {
    private CourseInfo info;
    private List<CourseInfo> recommend;

    public CourseInfo getInfo() {
        return info;
    }

    public void setInfo(CourseInfo info) {
        this.info = info;
    }

    public List<CourseInfo> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<CourseInfo> recommend) {
        this.recommend = recommend;
    }
}
