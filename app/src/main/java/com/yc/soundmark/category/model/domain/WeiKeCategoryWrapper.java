package com.yc.soundmark.category.model.domain;

import java.util.List;

/**
 * Created by wanglin  on 2018/10/25 11:35.
 */
public class WeiKeCategoryWrapper {
    private List<WeiKeCategory> list;
    private int count;

    public List<WeiKeCategory> getList() {
        return list;
    }

    public void setList(List<WeiKeCategory> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
