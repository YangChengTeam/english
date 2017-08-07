package com.yc.english.read.model.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by admin on 2017/7/26.
 * 年级
 */

@Entity
public class GradeInfo implements MultiItemEntity {

    public static final int CLICK_ITEM_VIEW = 1;

    @Id
    private Long id;

    private String gradeId;

    /**
     * 年级名称
     */

    private String gradeName;

    public int Type;

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public GradeInfo(final int type) {
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
