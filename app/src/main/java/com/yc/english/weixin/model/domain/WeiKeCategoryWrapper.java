package com.yc.english.weixin.model.domain;

import java.util.List;

/**
 * Created by zhangkai on 2017/9/6.
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
