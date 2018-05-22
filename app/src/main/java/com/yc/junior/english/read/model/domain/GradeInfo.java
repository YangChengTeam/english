package com.yc.junior.english.read.model.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by admin on 2017/7/26.
 * 年级
 */
@Entity
public class GradeInfo implements MultiItemEntity {

    public static final int CLICK_ITEM_VIEW = 1;

    @Id
    private Long id;

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

    @Generated(hash = 712582178)
    public GradeInfo(Long id, String name, String grade, boolean isSelected,
            String partType, int Type) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.isSelected = isSelected;
        this.partType = partType;
        this.Type = Type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
