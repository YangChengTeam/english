package com.yc.english.read.model.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by admin on 2017/7/26.
 */

public class LetterInfo implements MultiItemEntity {
    public static final int CLICK_ITEM_VIEW = 1;

    private String letterName;

    public String getLetterName() {
        return letterName;
    }

    public void setLetterName(String letterName) {
        this.letterName = letterName;
    }

    public int Type;

    public LetterInfo() {
        super();
    }

    public LetterInfo(final int type) {
        Type = type;
    }

    @Override
    public int getItemType() {
        return Type;
    }
}
