package com.yc.english.group.model.bean;

/**
 * Created by wanglin  on 2017/7/24 18:03.
 * 班级信息
 */

public class ClassInfo {

    private String imageUrl;
    private String className;
    private Integer count;
    private int groupId;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
