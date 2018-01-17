package com.yc.junior.english.read.model.domain;

import java.util.List;

/**
 * Created by admin on 2017/7/26.
 * 年级
 */


public class GradeInfoList {

    public static final int CLICK_ITEM_VIEW = 1;

    public List<GradeInfo> list;

    public List<GradeInfo> getList() {
        return list;
    }

    public void setList(List<GradeInfo> list) {
        this.list = list;
    }
}
