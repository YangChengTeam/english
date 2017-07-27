package com.yc.english.read.model.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by admin on 2017/7/26.
 */

public class UnitInfo implements MultiItemEntity {
    public static final int CLICK_ITEM_VIEW = 1;

    private String unitTitle;

    private String unitTotal;

    private String reciteTotalPersion;

    public String getUnitTitle() {
        return unitTitle;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    public String getUnitTotal() {
        return unitTotal;
    }

    public void setUnitTotal(String unitTotal) {
        this.unitTotal = unitTotal;
    }

    public String getReciteTotalPersion() {
        return reciteTotalPersion;
    }

    public void setReciteTotalPersion(String reciteTotalPersion) {
        this.reciteTotalPersion = reciteTotalPersion;
    }

    public int Type;

    public UnitInfo() {
        super();
    }

    public UnitInfo(final int type) {
        Type = type;
    }

    @Override
    public int getItemType() {
        return Type;
    }
}
