package com.yc.english.read.model.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by admin on 2017/7/20.
 * 英语课程
 */

public class EnglishCourseInfo implements MultiItemEntity {

    public static final int CLICK_ITEM_VIEW = 1;

    private String subtitle;
    private String bookid;
    private String sectionid;
    private String title;
    private String timepoint;
    private String course;
    private String fromarticle;
    private String unitid;
    private String subtitlecn;
    private String keypattern;

    private boolean isPlay = false;

    public EnglishCourseInfo(){
        super();
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getSectionid() {
        return sectionid;
    }

    public void setSectionid(String sectionid) {
        this.sectionid = sectionid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimepoint() {
        return timepoint;
    }

    public void setTimepoint(String timepoint) {
        this.timepoint = timepoint;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getFromarticle() {
        return fromarticle;
    }

    public void setFromarticle(String fromarticle) {
        this.fromarticle = fromarticle;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getSubtitlecn() {
        return subtitlecn;
    }

    public void setSubtitlecn(String subtitlecn) {
        this.subtitlecn = subtitlecn;
    }

    public String getKeypattern() {
        return keypattern;
    }

    public void setKeypattern(String keypattern) {
        this.keypattern = keypattern;
    }

    public int Type;

    public EnglishCourseInfo(final int type) {
        Type = type;
    }

    @Override
    public int getItemType() {
        return Type;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}
