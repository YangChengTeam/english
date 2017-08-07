package com.yc.english.read.model.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by admin on 2017/7/26.
 * 年级
 */

@Entity
public class CourseVersionInfo implements MultiItemEntity {

    public static final int CLICK_ITEM_VIEW = 1;

    @Id
    private Long id;

    private String courseVersionId;

    /**
     * 教材版本
     */

    private String courseVersionName;

    public int Type;

    public String getCourseVersionId() {
        return courseVersionId;
    }

    public void setCourseVersionId(String courseVersionId) {
        this.courseVersionId = courseVersionId;
    }

    public String getCourseVersionName() {
        return courseVersionName;
    }

    public void setCourseVersionName(String courseVersionName) {
        this.courseVersionName = courseVersionName;
    }

    public CourseVersionInfo(final int type) {
        Type = type;
    }

    @Override
    public int getItemType() {
        return Type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return this.Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

}
