package com.yc.junior.english.read.model.domain;

import java.util.List;

/**
 * Created by admin on 2017/7/26.
 */

public class BookUnitInfoList {
    public static final int CLICK_ITEM_VIEW = 1;

    public List<BookUnitInfo> list;

    public List<BookUnitInfo> getList() {
        return list;
    }

    public void setList(List<BookUnitInfo> list) {
        this.list = list;
    }
}
