package com.yc.english.read.model.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by admin on 2017/7/20.
 * 英语课程
 */

public class EnglishCourseInfo implements MultiItemEntity {

    public static final int CLICK_ITEM_VIEW = 1;

    public int Type = CLICK_ITEM_VIEW;

    private String id;

    private String title;

    @JSONField(name = "sub_title")
    private String subTitle;

    @JSONField(name = "book_id")
    private String bookId;

    @JSONField(name = "unit_id")
    private String unitId;

    @JSONField(name = "section_id")
    private String sectionId;

    @JSONField(name = "s_name")
    private String sname;

    private String means;

    private String rid;

    @JSONField(name = "is_del")
    private String isDel;

    private boolean isPlay = false;

    private boolean isShow;//是否显示

    private boolean speakResult;//显示结果

    public EnglishCourseInfo() {
        super();
    }

    public EnglishCourseInfo(final int type) {
        Type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getMeans() {
        return means;
    }

    public void setMeans(String means) {
        this.means = means;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isSpeakResult() {
        return speakResult;
    }

    public void setSpeakResult(boolean speakResult) {
        this.speakResult = speakResult;
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
