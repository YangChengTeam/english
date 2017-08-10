package com.yc.english.read.model.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by admin on 2017/7/26.
 * 年级
 */

public class GradeInfo implements MultiItemEntity {

    public static final int CLICK_ITEM_VIEW = 1;

    private String name;

    private String grade;

    private boolean isSelected = false;

    @JSONField(name = "part_type")
    private String partType;

    public int Type = CLICK_ITEM_VIEW;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public GradeInfo(){}

    public GradeInfo(final int type) {
        Type = type;
    }

    @Override
    public int getItemType() {
        return Type;
    }

    public int getType() {
        return this.Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
