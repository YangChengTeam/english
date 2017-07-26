package com.yc.english.read.model.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by admin on 2017/7/26.
 */

public class CommonInfo implements MultiItemEntity {
    public static final int CLICK_ITEM_VIEW = 1;

    private String commonName;

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public int Type;

    public CommonInfo() {
        super();
    }

    public CommonInfo(final int type) {
        Type = type;
    }

    @Override
    public int getItemType() {
        return Type;
    }
}
